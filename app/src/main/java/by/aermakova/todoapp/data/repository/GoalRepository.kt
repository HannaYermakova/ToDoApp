package by.aermakova.todoapp.data.repository

import by.aermakova.todoapp.data.db.dao.*
import by.aermakova.todoapp.data.db.database.GoalsDataBase
import by.aermakova.todoapp.data.db.entity.GoalEntity
import by.aermakova.todoapp.data.db.entity.KeyResultEntity
import by.aermakova.todoapp.data.model.*
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class GoalRepository (
    private val goalDao: GoalDao,
    private val keyResultDao: KeyResultDao,
    private val stepDao: StepDao,
    private val taskDao: TaskDao,
    private val ideaDao: IdeaDao,
    private val database: GoalsDataBase
) {

    fun saveGoalInLocalDataBase(goalEntity: GoalEntity): Long {
        return goalDao.insertGoal(goalEntity)
    }

    fun saveGoalsInLocalDataBase(goalEntities: List<GoalEntity>) {
        goalDao.insertAllIGoalsTransaction(goalEntities)
    }

    fun saveKeyResults(keyResultEntities: List<KeyResultEntity>) {
        keyResultDao.insertAllKeyResultsTransaction(keyResultEntities)
    }

    fun getAllGoalsWithKeyResults() = goalDao.getAllGoalsWithKeyResults()

    fun getAllUndoneGoals() = goalDao.getAllUndoneGoals()

    fun getKeyResultById(keyResultId: Long) = keyResultDao.getKeyResultById(keyResultId)

    fun getGoalWithKeyResultsById(id: Long) =
        goalDao.getGoalWithKeyResultsById(id)

    private fun getStepWithTasks(stepId: List<Long>) = stepDao.getStepsWithTasksById(stepId)
        .map { stepTasks ->
            stepTasks.map { stepTask ->
                stepTask.toCommonModel(stepTask.tasks.map { task -> task.toTaskTextModel() })
            }
        }

    private fun getKeyResultWithInnerItems(
        keyResId: Long,
        action: FunctionSelect
    ) = goalDao.getKeyResultWithStepsById(keyResId).flatMap { keyResultStep ->
        getStepWithTasks(keyResultStep.steps.map { step -> step.stepId })
            .flatMap { listStepModel ->
                taskDao.getTasksUnattachedToStep(keyResultStep.keyResult.keyResultId)
                    .map { listTaskEntity ->
                        val list = arrayListOf<CommonModel>()
                        list.addAll(listStepModel)
                        list.addAll(listTaskEntity.map { it.toTaskTextModel() })
                        keyResultStep.keyResult.toCommonModel(list, action)
                    }
            }
    }

    fun getGoalWithInnerItems(goalId: Long, action: FunctionSelect): Observable<GoalModel> {
        return goalDao.getGoalWithKeyResultsById(goalId)
            .flatMap { goalKeyRes ->
                val listOfKeyResults = arrayListOf<CommonModel>()
                goalKeyRes.keyResults.forEach {
                    getKeyResultWithInnerItems(it.keyResultId, action)
                        .subscribe { keyResultModel ->
                            listOfKeyResults.add(keyResultModel)
                        }
                }
                taskDao.getTasksUnattachedToKeyResult(goalId)
                    .map { listTaskEntity ->
                        val list = arrayListOf<CommonModel>()
                        list.addAll(listTaskEntity.map { it.toTaskTextModel() })
                        list.addAll(listOfKeyResults)
                        list
                    }.doOnNext { list ->
                        ideaDao.getIdeasByGoalId(goalId)
                            .subscribe { ideas ->
                                list.addAll(ideas.map { it.toCommonModel() })
                            }
                    }
                    .map {
                        goalKeyRes.toCommonModel(it)
                    }
            }
    }

    fun getGoalById(goalId: Long) = goalDao.getObsGoalById(goalId)

    fun updateGoalStatus(status: Boolean, goalId: Long) {
        goalDao.updateAllGoalItemsStatus(status, goalId)
    }

    fun getKeyResultsByGoalId(goalId: Long): Single<List<KeyResultEntity>> {
        return goalDao.getKeyResultByGoalId(goalId)
    }

    fun updateKeyResults(status: Boolean, keyResultIds: List<Long>) {
        goalDao.updateKeyResultsInGoal(status, keyResultIds)
    }

    fun getKeyResultByIds(keyResIds: List<Long>) = goalDao.getKeyResultsByIds(keyResIds)

    fun addNewKeyResult(keyResultEntity: KeyResultEntity): Long =
        keyResultDao.insertKeyResult(keyResultEntity)

    fun removeAll() = goalDao.deleteAll()

    fun checkGoalDone(goalId: Long) =
        goalDao.checkGoalDone(goalId)

    fun deleteGoalAndAllItsItems(goalId: Long) {
        database.runInTransaction {
            goalDao.deleteGoalById(goalId)
            keyResultDao.deleteKeyResultByGoalId(goalId)
            stepDao.deleteStepByGoalId(goalId)
            taskDao.deleteTaskByGoalId(goalId)
            ideaDao.deleteIdeaByGoalId(goalId)
        }
    }

    fun deleteStepAndAllItsItems(stepId: Long) {
        database.runInTransaction {
            stepDao.deleteStepById(stepId)
            taskDao.deleteTaskByStepId(stepId)
            ideaDao.selectIdeaByStepId(stepId)
        }
    }

    fun getAllKeyResultsIdByGoalId(goalId: Long): Single<List<Long>> {
        return goalDao.getAllKeyResultsIdByGoalId(goalId)
    }

    fun updateGoalText(newGoalText: String, goalId: Long): Boolean {
        return goalDao.updateGoalText(newGoalText, goalId) > 0
    }
}
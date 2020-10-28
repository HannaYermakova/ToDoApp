package by.aermakova.todoapp.data.repository

import by.aermakova.todoapp.data.db.dao.GoalDao
import by.aermakova.todoapp.data.db.dao.KeyResultDao
import by.aermakova.todoapp.data.db.dao.StepDao
import by.aermakova.todoapp.data.db.dao.TaskDao
import by.aermakova.todoapp.data.db.entity.GoalEntity
import by.aermakova.todoapp.data.db.entity.GoalKeyResults
import by.aermakova.todoapp.data.db.entity.KeyResultEntity
import by.aermakova.todoapp.data.db.entity.toCommonModel
import by.aermakova.todoapp.ui.adapter.*
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class GoalRepository @Inject constructor(
    private val goalDao: GoalDao,
    private val keyResultDao: KeyResultDao,
    private val stepDao: StepDao,
    private val taskDao: TaskDao
) {

    fun saveGoalInLocalDataBase(goalEntity: GoalEntity): Long {
        return goalDao.insertGoal(goalEntity)
    }

    fun saveGoalsInLocalDataBase(goalEntities: List<GoalEntity>) {
        goalDao.insertAllGoals(goalEntities)
    }

    fun saveKeyResults(keyResultEntities: List<KeyResultEntity>) {
        keyResultDao.insertAllKeyResults(keyResultEntities)
    }

    fun getAllGoalsWithKeyResults(): Observable<List<GoalKeyResults>> {
        return goalDao.getAllGoalsWithKeyResults()
    }

    fun getAllGoals(): Observable<List<GoalEntity>> {
        return goalDao.getAllGoals()
    }

    fun getKeyResultById(keyResultId: Long): Observable<KeyResultEntity> {
        return keyResultDao.getKeyResultById(keyResultId)
    }

    fun getGoalWithKeyResultsById(id: Long): Single<GoalKeyResults> {
        return goalDao.getGoalWithKeyResultsById(id)
    }

    private fun getStepWithTasks(stepId: List<Long>): Single<List<StepInGoalModel>> {
        return stepDao.getStepsWithTasksById(stepId)
            .map { stepTasks ->
                stepTasks
                    .map { stepTask ->
                        stepTask.toCommonModel(
                            stepTask.tasks
                                .map { task -> task.toTextModel() })
                    }
            }
    }

    private fun getKeyResultWithInnerItems(keyResId: Long): Single<KeyResultModel> {
        return goalDao.getKeyResultWithStepsById(keyResId).flatMap { keyResultStep ->
            getStepWithTasks(keyResultStep.steps.map { step -> step.stepId })
                .flatMap { listStepModel ->
                    taskDao.getTasksUnattachedToStep(keyResultStep.keyResult.keyResultId)
                        .map { listTaskEntity ->
                            val list = arrayListOf<CommonModel>()
                            list.addAll(listStepModel)
                            list.addAll(listTaskEntity.map { it.toTextModel() })
                            keyResultStep.keyResult.toCommonModel(list)
                        }
                }
        }
    }

    fun getGoalWithInnerItems(goalId: Long): Single<GoalModel> {
        return goalDao.getGoalWithKeyResultsById(goalId)
            .flatMap { goalKeyRes ->
                val listOfKeyResults = arrayListOf<CommonModel>()
                goalKeyRes.keyResults.forEach {
                    getKeyResultWithInnerItems(it.keyResultId).subscribe { keyResultModel ->
                        listOfKeyResults.add(keyResultModel)
                    }
                }
                taskDao.getTasksUnattachedToKeyResult(goalId)
                    .map { listTaskEntity ->
                        val list = arrayListOf<CommonModel>()
                        list.addAll(listTaskEntity.map { it.toTextModel() })
                        list.addAll(listOfKeyResults)
                        goalKeyRes.toCommonModel(list)
                    }

            }
    }

    fun getGoalById(goalId: Long): Observable<GoalEntity> {
        return goalDao.getObsGoalById(goalId)
    }
}
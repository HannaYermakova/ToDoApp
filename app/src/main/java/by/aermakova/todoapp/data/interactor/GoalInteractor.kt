package by.aermakova.todoapp.data.interactor

import by.aermakova.todoapp.data.db.entity.GoalEntity
import by.aermakova.todoapp.data.db.entity.GoalKeyResults
import by.aermakova.todoapp.data.db.entity.KeyResultEntity
import by.aermakova.todoapp.data.model.FunctionSelect
import by.aermakova.todoapp.data.model.GoalModel
import by.aermakova.todoapp.data.model.TextModel
import by.aermakova.todoapp.data.model.toTextModel
import by.aermakova.todoapp.data.remote.RemoteDatabase
import by.aermakova.todoapp.data.remote.model.*
import by.aermakova.todoapp.data.remote.sync.RemoteSync
import by.aermakova.todoapp.data.repository.GoalRepository
import by.aermakova.todoapp.data.repository.StepRepository
import by.aermakova.todoapp.data.repository.TaskRepository
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class GoalInteractor(
    private val goalRepository: GoalRepository,
    private val stepRepository: StepRepository,
    private val taskRepository: TaskRepository,
    private val goalsRemoteDatabase: RemoteDatabase<GoalRemoteModel>,
    private val keyResRemoteDatabase: RemoteDatabase<KeyResultRemoteModel>,
    private val stepRemoteDatabase: RemoteDatabase<StepRemoteModel>,
    private val taskRemoteDatabase: RemoteDatabase<TaskRemoteModel>
) : RemoteSync<GoalRemoteModel> {

    fun saveGoalAndKeyResToLocal(
        goalTitle: String,
        keyResults: List<String>
    ): Long {
        val goalId = goalRepository.saveGoalInLocalDataBase(
            GoalEntity(
                goalStatusDone = false,
                text = goalTitle
            )
        )
        goalRepository.saveKeyResults(keyResults.map { text ->
            KeyResultEntity(
                keyResultGoalId = goalId,
                text = text
            )
        })
        return goalId
    }

    fun getGoalKeyResultsById(goalId: Long): Single<GoalKeyResults> {
        return goalRepository.getGoalWithKeyResultsById(goalId)
    }

    fun saveGoalAndKeyResultsToRemote(goalKeyResults: GoalKeyResults) {
        goalsRemoteDatabase.saveData(goalKeyResults.goal.toRemote())
        val list = goalKeyResults.keyResults.map { it.toRemote() }
        for (model in list) {
            keyResRemoteDatabase.saveData(model)
        }
    }

    fun getAllGoalsWithKeyResults(): Observable<List<GoalKeyResults>> {
        return goalRepository.getAllGoalsWithKeyResults()
    }

    fun getAllGoals(): Observable<List<GoalEntity>> {
        return goalRepository.getAllGoals()
    }

    fun getAllUndoneGoals(): Observable<List<GoalEntity>> {
        return goalRepository.getAllUndoneGoals()
    }

    fun getGoalWithKeyResultsAndUnattachedTasks(
        goalId: Long,
        action: FunctionSelect
    ): Single<GoalModel> {
        return goalRepository.getGoalWithInnerItems(goalId, action)
    }

    fun updateGoal(status: Boolean, goalId: Long): Boolean {
        goalRepository.updateGoal(status, goalId)
        return true
    }

    fun updateKeyResults(status: Boolean, keyResultIds: List<Long>): Boolean {
        goalRepository.updateKeyResults(status, keyResultIds)
        return true
    }

    fun getKeyResultsById(keyResultId: Long): Observable<KeyResultEntity> {
        return goalRepository.getKeyResultById(keyResultId)
    }

    fun getGoalById(goalId: Long): Observable<GoalEntity> {
        return goalRepository.getGoalById(goalId)
    }

    fun updateGoalToRemote(goal: GoalEntity?) {
        goal?.let {
            goalsRemoteDatabase.updateData(it.toRemote())
            goalRepository.getKeyResultsByGoalId(goal.goalId).map { list ->
                list.map { keyResultEntity -> keyResRemoteDatabase.updateData(keyResultEntity.toRemote()) }
            }
            stepRepository.getStepsByGoalId(goal.goalId).map { list ->
                list.map { stepEntity -> stepRemoteDatabase.updateData(stepEntity.toRemote()) }
            }

            taskRepository.getTasksByGoalId(goal.goalId).map { list ->
                list.map { taskEntity -> taskRemoteDatabase.updateData(taskEntity.toRemote()) }
            }
        }
    }

    fun getKeyResultsByIds(keyResIds: List<Long>): Single<List<KeyResultEntity>> {
        return goalRepository.getKeyResultByIds(keyResIds)
    }

    fun createKeyResultsList(goalId: Long, _keyResultsList: Observer<List<TextModel>>): Disposable {
        return getGoalKeyResultsById(goalId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.keyResults.map { entity ->
                    entity.toTextModel()
                }
            }
            .subscribe(
                { _keyResultsList.onNext(it) },
                { it.printStackTrace() }
            )
    }

    fun updateKeyResultsToRemote(
        listKeyResultEntities: List<KeyResultEntity>?,
        keyResIds: List<Long>
    ) {
        listKeyResultEntities?.let {
            it.map { keyResultEntity -> keyResRemoteDatabase.updateData(keyResultEntity.toRemote()) }

            stepRepository.getStepsByKeyResultIds(keyResIds).map { list ->
                list.map { stepEntity -> stepRemoteDatabase.updateData(stepEntity.toRemote()) }
            }

            taskRepository.getTasksByKeyResultIds(keyResIds).map { list ->
                list.map { taskEntity -> taskRemoteDatabase.updateData(taskEntity.toRemote()) }
            }

        }
    }

    override fun addItemsDataListener(dataObserver: Observer<List<GoalRemoteModel>>) {
        goalsRemoteDatabase.addDataListener(dataObserver)
    }

    override fun saveItemsInLocalDatabase(list: List<GoalRemoteModel>) {
        goalRepository.saveGoalsInLocalDataBase(list.map { it.toLocal() })
    }

    fun removeAllFromLocalDatabase() = goalRepository.removeAll()
}
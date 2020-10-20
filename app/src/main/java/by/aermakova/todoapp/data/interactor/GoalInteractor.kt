package by.aermakova.todoapp.data.interactor

import by.aermakova.todoapp.data.db.entity.GoalEntity
import by.aermakova.todoapp.data.db.entity.GoalKeyResults
import by.aermakova.todoapp.data.db.entity.KeyResultEntity
import by.aermakova.todoapp.data.remote.RemoteDatabase
import by.aermakova.todoapp.data.remote.model.GoalRemoteModel
import by.aermakova.todoapp.data.remote.model.KeyResultRemoteModel
import by.aermakova.todoapp.data.remote.model.toLocal
import by.aermakova.todoapp.data.remote.model.toRemote
import by.aermakova.todoapp.data.repository.GoalRepository
import io.reactivex.Observable
import io.reactivex.Observer

class GoalInteractor(
    private val goalRepository: GoalRepository,
    private val goalsRemoteDatabase: RemoteDatabase<GoalRemoteModel>,
    private val keyResRemoteDatabase: RemoteDatabase<KeyResultRemoteModel>
) {
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

    fun getGoalKeyResultsById(goalId: Long): Observable<GoalKeyResults> {
        return goalRepository.getGoalWithKeyResultsById(goalId)
    }

    fun saveGoalAndKeyResultsToRemote(goalKeyResults: GoalKeyResults) {
        goalsRemoteDatabase.saveData(goalKeyResults.goal.toRemote())
        val list = goalKeyResults.keyResults.map { it.toRemote() }
        for (model in list) {
            keyResRemoteDatabase.saveData(model)
        }
    }

    fun addGoalsDataListener(dataObserver: Observer<List<GoalRemoteModel>>) {
        goalsRemoteDatabase.addDataListener(dataObserver)
    }

    fun addKeyResultsDataListener(dataObserver: Observer<List<KeyResultRemoteModel>>) {
        keyResRemoteDatabase.addDataListener(dataObserver)
    }

    fun getAllGoalsWithKeyResultsWithoutConverting(): Observable<List<GoalKeyResults>> {
        return goalRepository.getAllGoalsWithKeyResults()
    }

    fun getGoalWithKeyResultsById(id: Long): Observable<GoalKeyResults> {
        return goalRepository.getGoalWithKeyResultsById(id)
    }

    fun saveGoalsInLocalDatabase(collection: List<GoalRemoteModel>) {
        goalRepository.saveGoalsInLocalDataBase(collection.map { it.toLocal() })
    }

    fun saveKeyResultsInLocalDatabase(list: List<KeyResultRemoteModel>) {
        goalRepository.saveKeyResults(list.map { it.toLocal() })
    }
}
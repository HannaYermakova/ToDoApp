package by.aermakova.todoapp.data.interactor

import android.util.Log
import by.aermakova.todoapp.data.db.entity.GoalEntity
import by.aermakova.todoapp.data.db.entity.GoalKeyResults
import by.aermakova.todoapp.data.db.entity.KeyResultEntity
import by.aermakova.todoapp.data.db.entity.toModel
import by.aermakova.todoapp.data.model.Goal
import by.aermakova.todoapp.data.remote.RemoteDatabase
import by.aermakova.todoapp.data.remote.model.GoalRemoteModel
import by.aermakova.todoapp.data.remote.model.KeyResultRemoteModel
import by.aermakova.todoapp.data.remote.model.toLocal
import by.aermakova.todoapp.data.remote.model.toRemote
import by.aermakova.todoapp.data.repository.GoalRepository
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single

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

    fun getGoalKeyResultsById(goalId:Long) : Observable<GoalKeyResults>{
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

    fun getAllGoalsWithKeyResults(): Observable<List<Goal>> {
        return goalRepository.getAllGoalsWithKeyResults().map { list -> list.map { it.toModel() } }
    }

    fun getGoalWithKeyResultsById(id: Long): Observable<Goal> {
        return goalRepository.getGoalWithKeyResultsById(id).map { it.toModel() }
    }

    fun saveGoalsInLocalDatabase(collection: List<GoalRemoteModel>) {
        goalRepository.saveGoalsInLocalDataBase(collection.map { it.toLocal() })
    }

    fun saveKeyResultsInLocalDatabase(list: List<KeyResultRemoteModel>) {
        goalRepository.saveKeyResults(list.map { it.toLocal() })
    }
}
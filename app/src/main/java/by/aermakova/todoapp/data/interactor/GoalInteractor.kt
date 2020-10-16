package by.aermakova.todoapp.data.interactor

import by.aermakova.todoapp.data.db.entity.GoalEntity
import by.aermakova.todoapp.data.db.entity.KeyResultEntity
import by.aermakova.todoapp.data.db.entity.toModel
import by.aermakova.todoapp.data.model.Goal
import by.aermakova.todoapp.data.remote.RemoteDatabase
import by.aermakova.todoapp.data.remote.model.GoalRemoteModel
import by.aermakova.todoapp.data.remote.model.toRemote
import by.aermakova.todoapp.data.repository.GoalRepository
import io.reactivex.Observable
import io.reactivex.Observer

class GoalInteractor (
    private val goalRepository: GoalRepository,
    private val remoteDatabase: RemoteDatabase<GoalRemoteModel>
) {
    fun saveAndSyncGoalAndKeyRes(goalTitle: String, keyResults: List<String>) {
        val goalId = goalRepository.saveGoalInLocalDataBase(
            GoalEntity(
                goalStatusDone = false,
                text = goalTitle
            )
        )
        val goalEntity = goalRepository.getGoalById(goalId)
        remoteDatabase.saveData(goalEntity.toRemote())

        goalRepository.saveKeyResults(keyResults.map { text ->
            KeyResultEntity(
                keyResultGoalId = goalId,
                text = text
            )
        })
    }

    fun addDataListener(dataObserver: Observer<Collection<GoalRemoteModel>>){
        remoteDatabase.addDataListener(dataObserver)
    }

    fun getAllGoalsWithKeyResults() : Observable<List<Goal>>{
        return goalRepository.getAllGoalsWithKeyResults().map { list -> list.map { it.toModel() } }
    }

    fun getGoalWithKeyResultsById(id: Long) : Observable<Goal>{
        return goalRepository.getGoalWithKeyResultsById(id).map { it.toModel() }
    }
}
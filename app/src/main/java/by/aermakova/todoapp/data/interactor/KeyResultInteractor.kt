package by.aermakova.todoapp.data.interactor

import by.aermakova.todoapp.data.remote.DeleteGoalItems
import by.aermakova.todoapp.data.remote.RemoteDatabase
import by.aermakova.todoapp.data.remote.model.KeyResultRemoteModel
import by.aermakova.todoapp.data.remote.model.toLocal
import by.aermakova.todoapp.data.remote.sync.RemoteSync
import by.aermakova.todoapp.data.repository.GoalRepository
import io.reactivex.Observer
import io.reactivex.Single


class KeyResultInteractor(
    private val goalRepository: GoalRepository,
    private val keyResRemoteDatabase: RemoteDatabase<KeyResultRemoteModel>
) : RemoteSync<KeyResultRemoteModel>, DeleteGoalItems {

    override fun addItemsDataListener(dataObserver: Observer<List<KeyResultRemoteModel>>) {
        keyResRemoteDatabase.addDataListener(dataObserver)
    }

    override fun saveItemsInLocalDatabase(list: List<KeyResultRemoteModel>) {
        goalRepository.saveKeyResults(list.map { it.toLocal() })
    }

    override fun deleteGoalsItemsById(goalId: Long) =
        goalRepository.getAllKeyResultsIdByGoalId(goalId).map { ids ->
            ids.map { keyResRemoteDatabase.removeData(it) }
        }
}
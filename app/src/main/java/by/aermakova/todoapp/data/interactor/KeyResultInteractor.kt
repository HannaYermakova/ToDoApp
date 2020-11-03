package by.aermakova.todoapp.data.interactor

import by.aermakova.todoapp.data.remote.RemoteDatabase
import by.aermakova.todoapp.data.remote.model.KeyResultRemoteModel
import by.aermakova.todoapp.data.remote.model.toLocal
import by.aermakova.todoapp.data.remote.sync.RemoteSync
import by.aermakova.todoapp.data.repository.GoalRepository
import io.reactivex.Observer


class KeyResultInteractor(
    private val goalRepository: GoalRepository,
    private val keyResRemoteDatabase: RemoteDatabase<KeyResultRemoteModel>
) : RemoteSync<KeyResultRemoteModel> {

    override fun addItemsDataListener(dataObserver: Observer<List<KeyResultRemoteModel>>) {
        keyResRemoteDatabase.addDataListener(dataObserver)
    }

    override fun saveItemsInLocalDatabase(list: List<KeyResultRemoteModel>) {
        goalRepository.saveKeyResults(list.map { it.toLocal() })
    }
}
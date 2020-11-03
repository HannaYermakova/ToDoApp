package by.aermakova.todoapp.data.remote.sync

import by.aermakova.todoapp.data.remote.model.BaseRemoteModel
import io.reactivex.Observer


interface RemoteSync<RemoteModel : BaseRemoteModel> {
    fun addItemsDataListener(dataObserver: Observer<List<RemoteModel>>)
    fun saveItemsInLocalDatabase(list: List<RemoteModel>)
}
package by.aermakova.todoapp.data.remote

import by.aermakova.todoapp.data.remote.model.BaseRemoteModel
import io.reactivex.Observer

interface RemoteDatabase<Type : BaseRemoteModel> {

    fun saveData(data: Type)
    fun updateData(data: Type)
    fun addDataListener(dataObserver: Observer<List<Type>>)
    fun removeData(id: Long)
}
package by.aermakova.todoapp.data.remote

import io.reactivex.Observer

interface RemoteDatabase<Type> {

    fun saveData(data: Type)
    fun addDataListener(dataObserver: Observer<Collection<Type>>)
}
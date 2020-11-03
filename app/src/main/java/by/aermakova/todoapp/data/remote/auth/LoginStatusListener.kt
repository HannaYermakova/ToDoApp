package by.aermakova.todoapp.data.remote.auth

interface LoginStatusListener {
    fun onSuccess()
    fun onCancel()
    fun onError()
}
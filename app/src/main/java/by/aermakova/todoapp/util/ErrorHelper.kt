package by.aermakova.todoapp.util


fun Throwable.handleError(errorMessage: String?, errorAction: ((String) -> Unit)? = null) {
    printStackTrace()
    val error :String = errorMessage?: message?: ""
    errorAction?.invoke(error)
}
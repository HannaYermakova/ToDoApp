package by.aermakova.todoapp.util


fun Throwable.handleError(errorMessage: String? = null, errorAction: ((String) -> Unit)? = null) {
    printStackTrace()
    val error :String = errorMessage?: message?: ""
    errorAction?.invoke(error)
}
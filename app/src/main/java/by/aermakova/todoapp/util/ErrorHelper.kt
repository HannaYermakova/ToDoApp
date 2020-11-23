package by.aermakova.todoapp.util

import by.aermakova.todoapp.data.model.FunctionString


fun Throwable.handleError(errorMessage: String? = null, errorAction: FunctionString? = null) {
    printStackTrace()
    val error: String = errorMessage ?: message ?: ""
    errorAction?.invoke(error)
}
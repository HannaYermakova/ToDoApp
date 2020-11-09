package by.aermakova.todoapp.data.model

typealias Function = (Long) -> Unit
typealias FunctionSelect = (Long, Boolean) -> Unit

open class CommonModel(
    val id: Long,
    val layout: Int,
    val variableId: Int,
    val clickAction: Function? = null
)
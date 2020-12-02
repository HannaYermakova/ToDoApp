package by.aermakova.todoapp.data.model

typealias FunctionLong = (Long) -> Unit
typealias FunctionString = (String) -> Unit
typealias FunctionSelect = (Long, Boolean) -> Unit
typealias FunctionNoArgs = () -> Unit

abstract class CommonModel(
    val id: Long,
    val layout: Int,
    val variableId: Int,
    val clickAction: FunctionLong? = null,
    val longClickAction: FunctionLong? = null
) {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + layout
        result = 31 * result + variableId
        result = 31 * result + (clickAction?.hashCode() ?: 0)
        result = 31 * result + (longClickAction?.hashCode() ?: 0)
        return result
    }
}
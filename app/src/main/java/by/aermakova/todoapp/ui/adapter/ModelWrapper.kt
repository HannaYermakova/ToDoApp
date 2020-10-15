package by.aermakova.todoapp.ui.adapter

import by.aermakova.todoapp.BR
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.model.Goal
import by.aermakova.todoapp.data.model.KeyResult

typealias Function = (Long) -> Unit

class ModelWrapper<Type> private constructor(
    val id: Long,
    val type: Type,
    val layout: Int,
    val variableId: Int,
    val clickAction: Function?
) {
    data class Builder<Type>(
        var id: Long,
        var type: Type,
        var layout: Int,
        var variableId: Int,
        var clickAction: Function? = null
    ) {

        fun clickAction(clickAction: Function) = apply { this.clickAction = clickAction }
        fun build() = ModelWrapper(id, type, layout, variableId, clickAction)
    }
}

fun <Type> toModel(
    id: Long,
    type: Type,
    layout: Int,
    variableId: Int
): ModelWrapper<Type> = ModelWrapper
    .Builder(id, type, layout, variableId)
    .build()


fun List<String>.toModelStringList(): List<ModelWrapper<String>> {
    var i = 0L
    return map { toModel(i++, it, R.layout.item_text_line, BR.text) }
}

fun KeyResult.toModelKeyResult(): ModelWrapper<KeyResult> {
    return ModelWrapper.Builder(keyResultId, this, R.layout.item_key_result, BR.keyResult).build()
}

fun List<Goal>.toModelGoalList(clickAction: Function): List<ModelWrapper<Goal>> {
    return map {
        ModelWrapper.Builder(it.goalId, it, R.layout.item_goal, BR.goalModel)
            .clickAction(clickAction).build()
    }
}
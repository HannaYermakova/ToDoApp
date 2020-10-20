package by.aermakova.todoapp.ui.adapter

import by.aermakova.todoapp.BR
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.db.entity.GoalKeyResults
import by.aermakova.todoapp.data.db.entity.toCommonModel

open class CommonModel(
    val id: Long,
    val layout: Int,
    val variableId: Int,
    val clickAction: Function? = null
)

data class GoalModel(
    val goalId: Long,
    val status: Boolean,
    val text: String,
    val keyResults: List<KeyResultModel>,
    val action: Function
) : CommonModel(goalId, R.layout.item_goal, BR.goalModel, action)

data class KeyResultModel(
    val keyResultId: Long,
    val goalId: Long,
    val status: Boolean,
    val text: String
) : CommonModel(keyResultId, R.layout.item_key_result, BR.keyResult)

data class TextModel(
    val textId: Long,
    val text: String
) : CommonModel(textId, R.layout.item_text_line, BR.text)

fun List<String>.toCommonModelStringList(): List<TextModel> {
    var i = 0L
    return map { it.toCommonModel(i++) }
}

fun String.toCommonModel(
    textId: Long
) = TextModel(textId, this)

fun GoalKeyResults.toCommonModel(action: Function): GoalModel {
    return with(goal) { GoalModel(goalId, goalStatusDone, text, keyResults.map { it.toCommonModel() }, action) }
}

fun List<GoalKeyResults>.toCommonModelGoalList(clickAction: Function): List<GoalModel> {
    return map {
        it.toCommonModel(clickAction)
    }
}
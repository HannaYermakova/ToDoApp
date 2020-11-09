package by.aermakova.todoapp.data.model

import android.content.res.Resources
import by.aermakova.todoapp.BR
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.db.entity.*
import by.aermakova.todoapp.util.TaskFilterItem
import by.aermakova.todoapp.util.TaskSortItem

data class TextModel(
    val textId: Long,
    val text: String,
    val action: Function? = null,
    var selected: Boolean = false
) : CommonModel(textId, R.layout.item_text_line, BR.text, action) {
    override fun toString() = text
}

fun String.toCommonModel(
    textId: Long,
    action: Function? = null
) = TextModel(textId, this, action)

fun List<String>.toCommonModelStringList(): List<TextModel> {
    var i = 0L
    return map { it.toCommonModel(i++) }
}

fun TaskFilterItem.toTextModel(
    res: Resources,
    selected: Boolean = false,
    clickAction: Function
): TextModel {
    return TextModel(
        listId.toLong(),
        res.getString(listId),
        selected = selected,
        action = clickAction
    )
}

fun TaskSortItem.toTextModel(
    res: Resources,
    selected: Boolean = false,
    clickAction: Function
): TextModel {
    return TextModel(
        listId.toLong(),
        res.getString(listId),
        selected = selected,
        action = clickAction
    )
}

fun GoalEntity.toTextModel(clickAction: Function? = null): TextModel {
    return TextModel(goalId, text, action = clickAction)
}

fun KeyResultEntity.toTextModel(clickAction: Function? = null): TextModel {
    return TextModel(keyResultId, text, action = clickAction)
}

fun StepEntity.toTextModel(clickAction: Function? = null): TextModel {
    return TextModel(stepId, text, action = clickAction)
}

fun TaskEntity.toTextModel(clickAction: Function? = null): TextModel {
    return TextModel(
        taskId,
        text,
        clickAction
    )
}

fun IdeaEntity.toTextModel(clickAction: Function? = null): TextModel {
    return TextModel(
        ideaId,
        text,
        clickAction
    )
}
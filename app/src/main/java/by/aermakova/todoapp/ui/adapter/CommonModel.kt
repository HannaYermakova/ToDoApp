package by.aermakova.todoapp.ui.adapter

import android.content.res.Resources
import by.aermakova.todoapp.BR
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.db.entity.*
import by.aermakova.todoapp.util.TaskFilterItem
import by.aermakova.todoapp.util.TaskSortItem

typealias Function = (Long) -> Unit

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
    val keyResults: List<KeyResultModel>? = null,
    val action: Function? = null
) : CommonModel(goalId, R.layout.item_goal, BR.goalModel, action)

data class KeyResultModel(
    val keyResultId: Long,
    val goalId: Long,
    val status: Boolean,
    val text: String
) : CommonModel(keyResultId, R.layout.item_key_result, BR.keyResult)

data class TaskModel(
    val taskId: Long,
    val status: Boolean,
    val text: String,
    val goalId: Long?,
    val keyResultId: Long?,
    val stepId: Long?,
    val finishTime: Long?,
    val startTime: Long,
    val scheduledTask: Boolean,
    val interval: Int?,
    val action: Function? = null
) : CommonModel(taskId, R.layout.item_task, BR.task, action)

data class StepModel(
    val stepId: Long,
    val keyResultId: Long,
    val goalId: Long,
    val status: Boolean,
    val text: String,
    val action: Function? = null
) : CommonModel(stepId, R.layout.item_step, BR.step, action)

data class IdeaModel(
    val ideaId: Long,
    val goalId: Long,
    val keyResultId: Long,
    val text: String,
    val action: Function? = null
) : CommonModel(ideaId, R.layout.item_idea, BR.idea, action)

data class TextModel(
    val textId: Long,
    val text: String,
    val action: Function? = null,
    var selected: Boolean = false
) : CommonModel(textId, R.layout.item_text_line, BR.text, action)

data class EmptyModel(
    val textId: Long = 0,
    val text: String ="" ,
) : CommonModel(textId, R.layout.item_empty_line, BR.empty)

fun List<String>.toCommonModelStringList(): List<TextModel> {
    var i = 0L
    return map { it.toCommonModel(i++) }
}

fun String.toCommonModel(
    textId: Long,
    action: Function? = null
) = TextModel(textId, this, action)

fun GoalKeyResults.toCommonModel(action: Function): GoalModel {
    return with(goal) {
        GoalModel(
            goalId,
            goalStatusDone,
            text,
            keyResults.map { it.toCommonModel() },
            action
        )
    }
}

fun TaskFilterItem.toTextModel(res: Resources, selected: Boolean = false, clickAction: Function): TextModel {
    return TextModel(listId.toLong(), res.getString(listId), selected = selected, action = clickAction)
}

fun TaskSortItem.toTextModel(res: Resources, selected: Boolean = false, clickAction: Function): TextModel {
    return TextModel(listId.toLong(), res.getString(listId), selected = selected, action = clickAction)
}

fun GoalEntity.toTextModel(clickAction: Function): TextModel {
    return TextModel(goalId, text, action = clickAction)
}

fun KeyResultEntity.toTextModel(clickAction: Function): TextModel {
    return TextModel(keyResultId, text, action = clickAction)
}

fun StepEntity.toTextModel(clickAction: Function): TextModel {
    return TextModel(stepId, text, action = clickAction)
}

fun TaskEntity.toCommonModel(clickAction: Function): TaskModel {
    return TaskModel(
        taskId,
        taskStatusDone,
        text,
        taskGoalId,
        taskKeyResultId,
        taskStepId,
        finishDate,
        startTime,
        scheduledTask,
        interval,
        clickAction
    )
}

fun TaskEntity.toTextModel(clickAction: Function? = null): TextModel {
    return TextModel(
        taskId,
        text,
        clickAction
    )
}

fun StepEntity.toCommonModel(clickAction: Function): StepModel {
    return StepModel(stepId, stepKeyResultId, stepGoalId, stepStatusDone, text, clickAction)
}

fun IdeaEntity.toCommonModel(clickAction: Function): IdeaModel {
    return IdeaModel(ideaId, ideaGoalId, ideaKeyResultId, text, clickAction)
}

fun List<GoalKeyResults>.toCommonModelGoalList(clickAction: Function): List<GoalModel> {
    return map {
        it.toCommonModel(clickAction)
    }
}
package by.aermakova.todoapp.data.model

import by.aermakova.todoapp.BR
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.db.entity.TaskEntity
import by.aermakova.todoapp.util.convertLongToDate

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
    val action: FunctionLong? = null,
    val longClick: FunctionLong? = null
) : CommonModel(taskId, R.layout.item_task, BR.task, action, longClick) {

    val finishTimeText = finishTime?.let { convertLongToDate(it) }
    val hasDeadline: Boolean = if (finishTime != null) finishTime > 0 else false
}

fun TaskEntity.toCommonModel(
    clickAction: FunctionLong? = null,
    longClick: FunctionLong? = null
): TaskModel {
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
        clickAction,
        longClick
    )
}
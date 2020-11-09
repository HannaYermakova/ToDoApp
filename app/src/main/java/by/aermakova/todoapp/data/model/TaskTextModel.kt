package by.aermakova.todoapp.data.model

import by.aermakova.todoapp.BR
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.db.entity.TaskEntity

data class TaskTextModel(
    val textId: Long,
    val text: String,
    val status: Boolean
) : CommonModel(textId, R.layout.item_task_text_line, BR.task)

fun TaskEntity.toTaskTextModel(): TaskTextModel {
    return TaskTextModel(
        taskId,
        text,
        taskStatusDone
    )
}
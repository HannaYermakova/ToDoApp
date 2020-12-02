package by.aermakova.todoapp.data.remote.model

import by.aermakova.todoapp.data.db.entity.TaskEntity
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class TaskRemoteModel(

    val taskId: Long? = 0,
    val status: Boolean? = false,
    val text: String? = "",
    val goalId: Long? = null,
    val keyResultId: Long? = null,
    val stepId: Long? = null,
    val finishTime: Long? = null,
    val startTime: Long? = 0,
    val scheduledTask: Boolean? = false,
    val interval: Int? = null
) : BaseRemoteModel(taskId.toString())

fun TaskEntity.toRemote(): TaskRemoteModel =
    TaskRemoteModel(
        taskId,
        taskStatusDone,
        text,
        taskGoalId,
        taskKeyResultId,
        taskStepId,
        finishDate,
        startTime,
        scheduledTask,
        interval
    )

fun TaskRemoteModel.toLocal(): TaskEntity {
    return if (
        taskId != null &&
        status != null &&
        text != null &&
        startTime != null &&
        scheduledTask != null
    )
        TaskEntity(
        taskId,
        status,
        text,
        goalId,
        keyResultId,
        stepId,
        finishTime,
        startTime,
        scheduledTask,
        interval
    )
    else throw Exception("Task can't be null!")
}
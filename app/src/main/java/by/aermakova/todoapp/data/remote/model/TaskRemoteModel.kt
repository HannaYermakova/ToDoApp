package by.aermakova.todoapp.data.remote.model

import by.aermakova.todoapp.data.db.entity.TaskEntity
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class TaskRemoteModel(

    var taskId: Long? = 0,
    var status: Boolean? = false,
    var text: String? = "",
    var goalId: Long? = 0,
    var keyResultId: Long? = 0,
    var stepId: Long? = 0,
    var finishTime: Long? = 0,
    var startTime: Long? = 0,
    var scheduledTask: Boolean? = false,
    var interval: Int? = 0
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
        taskId!!,
        status!!,
        text!!,
        goalId,
        keyResultId,
        stepId,
        finishTime,
        startTime!!,
        scheduledTask!!,
        interval
    )
    else throw Exception("Task can't be null!")
}
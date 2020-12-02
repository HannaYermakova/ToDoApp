package by.aermakova.todoapp.data.remote.model

import by.aermakova.todoapp.data.db.entity.StepEntity
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class StepRemoteModel(

    val stepId: Long? = 0,

    val stepKeyResultId: Long? = 0,

    val stepGoalId: Long? = 0,

    val stepStatusDone: Boolean? = false,

    val text: String? = ""
) : BaseRemoteModel(stepId.toString())

fun StepEntity.toRemote(): StepRemoteModel =
    StepRemoteModel(
        stepId,
        stepKeyResultId,
        stepGoalId,
        stepStatusDone,
        text
    )

fun StepRemoteModel.toLocal(): StepEntity {
    return if (
        stepId != null &&
        stepKeyResultId != null &&
        stepGoalId != null &&
        stepStatusDone != null &&
        text != null
    )
        StepEntity(
            stepId!!,
            stepKeyResultId!!,
            stepGoalId!!,
            stepStatusDone!!,
            text!!
        )
    else throw Exception("Task can't be null!")
}
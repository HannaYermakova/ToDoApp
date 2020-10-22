package by.aermakova.todoapp.data.remote.model

import by.aermakova.todoapp.data.db.entity.StepEntity
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class StepRemoteModel(

    var stepId: Long? = 0,
    var stepKeyResultId: Long? = 0,
    var stepGoalId: Long? = 0,
    var stepStatusDone: Boolean? = false,
    var text: String? = ""
)

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
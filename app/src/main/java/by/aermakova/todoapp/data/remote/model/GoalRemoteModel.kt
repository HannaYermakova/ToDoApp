package by.aermakova.todoapp.data.remote.model

import by.aermakova.todoapp.data.db.entity.GoalEntity
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class GoalRemoteModel(

    var goalId: Long? = 0,

    var goalStatusDone: Boolean? = false,

    var text: String? = ""
)

internal fun GoalEntity.toRemote(): GoalRemoteModel {
    return GoalRemoteModel(goalId, goalStatusDone, text)
}

fun GoalRemoteModel.toLocal(): GoalEntity {
    return if (goalId != null && goalStatusDone != null && text != null)
        GoalEntity(goalId!!, goalStatusDone!!, text!!)
    else throw Exception("Goal can't be null!")
}

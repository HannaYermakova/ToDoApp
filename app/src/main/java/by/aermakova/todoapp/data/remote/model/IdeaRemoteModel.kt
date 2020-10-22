package by.aermakova.todoapp.data.remote.model

import by.aermakova.todoapp.data.db.entity.IdeaEntity
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class IdeaRemoteModel(

    var ideaId: Long? = 0,
    var goalId: Long? = 0,
    var text: String? = "",
)

fun IdeaEntity.toRemote(): IdeaRemoteModel =
    IdeaRemoteModel(
        ideaId,
        ideaGoalId,
        text
    )

fun IdeaRemoteModel.toLocal(): IdeaEntity {
    return if (
        ideaId != null &&
        goalId != null &&
        text != null
    )
        IdeaEntity(
            ideaId!!,
            goalId!!,
            text!!
        )
    else throw Exception("Task can't be null!")
}
package by.aermakova.todoapp.data.remote.model

import by.aermakova.todoapp.data.db.entity.IdeaEntity
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class IdeaRemoteModel(

    var ideaId: Long? = 0,

    var goalId: Long? = 0,

    var keyResultId: Long? = 0,

    var text: String? = "",

) : BaseRemoteModel(ideaId.toString())

fun IdeaEntity.toRemote(): IdeaRemoteModel =
    IdeaRemoteModel(
        ideaId,
        ideaGoalId,
        ideaKeyResultId,
        text
    )

fun IdeaRemoteModel.toLocal(): IdeaEntity {
    return if (
        ideaId != null &&
        goalId != null &&
        keyResultId != null &&
        text != null
    )
        IdeaEntity(
            ideaId!!,
            goalId!!,
            keyResultId!!,
            text!!
        )
    else throw Exception("Task can't be null!")
}
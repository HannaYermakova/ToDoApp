package by.aermakova.todoapp.data.remote.model

import by.aermakova.todoapp.data.db.entity.KeyResultEntity
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class KeyResultRemoteModel(

    var keyResultId: Long? = 0,

    var goalId: Long? = 0,

    var status: Boolean? = false,

    var text: String? = ""
)

fun KeyResultEntity.toRemote(): KeyResultRemoteModel {
    return KeyResultRemoteModel(keyResultId, keyResultGoalId, keyResultStatusDone, text)
}

fun KeyResultRemoteModel.toLocal(): KeyResultEntity {
    return if (
        keyResultId != null &&
        goalId != null &&
        status != null &&
        text != null
    )
        KeyResultEntity(
            keyResultId!!,
            goalId!!,
            status!!,
            text!!
        )
    else throw Exception("Goal can't be null!")
}
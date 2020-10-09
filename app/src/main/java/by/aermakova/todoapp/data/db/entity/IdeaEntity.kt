package by.aermakova.todoapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import by.aermakova.todoapp.data.db.database.IDEAS_TABLE_NAME
import com.google.gson.annotations.SerializedName

@Entity(tableName = IDEAS_TABLE_NAME)
data class IdeaEntity(

    @PrimaryKey
    @SerializedName("idea_id")
    val ideaId: Long,

    @SerializedName("goal_id")
    val goalId: Long?,

    @SerializedName("key_result_id")
    val keyResultId: Long?,

    @SerializedName("step_id")
    val stepId: Long?,

    val text: String
)
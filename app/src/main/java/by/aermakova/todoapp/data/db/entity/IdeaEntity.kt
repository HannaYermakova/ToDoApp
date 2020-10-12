package by.aermakova.todoapp.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.aermakova.todoapp.data.db.database.IDEAS_TABLE_NAME
import com.google.gson.annotations.SerializedName

@Entity(tableName = IDEAS_TABLE_NAME)
data class IdeaEntity(

    @PrimaryKey
    @ColumnInfo(name ="idea_id")
    val ideaId: Long,

    @ColumnInfo(name ="idea_goal_id")
    val ideaGoalId: Long?,

    @ColumnInfo(name ="idea_key_result_id")
    val ideaKeyResultId: Long?,

    @ColumnInfo(name ="idea_step_id")
    val ideaStepId: Long?,

    val text: String
)
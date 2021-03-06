package by.aermakova.todoapp.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.aermakova.todoapp.data.db.database.IDEAS_TABLE_NAME

@Entity(tableName = IDEAS_TABLE_NAME)
data class IdeaEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="idea_id")
    val ideaId: Long = 0,

    @ColumnInfo(name ="idea_goal_id")
    val ideaGoalId: Long,

    @ColumnInfo(name ="idea_key_result_id")
    val ideaKeyResultId: Long? = null,

    @ColumnInfo(name ="idea_step_id")
    val ideaStepId: Long? = null,

    val text: String
)
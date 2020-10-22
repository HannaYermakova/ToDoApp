package by.aermakova.todoapp.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.aermakova.todoapp.data.db.database.STEPS_TABLE_NAME
import com.google.gson.annotations.SerializedName

@Entity(tableName = STEPS_TABLE_NAME)
data class StepEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="step_id")
    val stepId: Long = 0,

    @ColumnInfo(name ="step_key_result_id")
    val stepKeyResultId: Long,

    @ColumnInfo(name ="step_goal_id")
    val stepGoalId: Long,

    @ColumnInfo(name ="step_status_done")
    val stepStatusDone: Boolean = false,

    val text: String
)
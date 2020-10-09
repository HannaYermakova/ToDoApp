package by.aermakova.todoapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import by.aermakova.todoapp.data.db.database.TASKS_TABLE_NAME
import com.google.gson.annotations.SerializedName

@Entity(tableName = TASKS_TABLE_NAME)
data class TaskEntity(

    @PrimaryKey(autoGenerate = true)
    @SerializedName("task_name")
    val taskId: Long,

    @SerializedName("status_done")
    val statusDone: Boolean,

    val text: String,

    @SerializedName("goal_id")
    val goalId: Long?,

    @SerializedName("key_result_id")
    val keyResultId: Long?,

    @SerializedName("step_id")
    val stepId: Long?,

    @SerializedName("finish_time")
    val finishTime: Long?,

    @SerializedName("start_time")
    val startTime: Long,

    @SerializedName("scheduled_task")
    val scheduledTask: Boolean,

    val interval: Long?
)
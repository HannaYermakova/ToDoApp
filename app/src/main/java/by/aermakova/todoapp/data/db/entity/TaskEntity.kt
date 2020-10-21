package by.aermakova.todoapp.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.aermakova.todoapp.data.db.database.TASKS_TABLE_NAME

@Entity(tableName = TASKS_TABLE_NAME)
data class TaskEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    val taskId: Long = 0,

    @ColumnInfo(name = "task_status_done")
    val taskStatusDone: Boolean = false,

    val text: String,

    @ColumnInfo(name = "task_goal_id")
    val taskGoalId: Long?,

    @ColumnInfo(name = "task_key_result_id")
    val taskKeyResultId: Long?,

    @ColumnInfo(name = "task_step_id")
    val taskStepId: Long?,

    @ColumnInfo(name = "finish_time")
    val finishDate: Long?,

    @ColumnInfo(name = "start_time")
    val startTime: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "scheduled_task")
    val scheduledTask: Boolean,

    val interval: Int?
)

enum class Interval(val code: Int) {
    DAILY(0),
    WEEKLY(1),
    MONTHLY(2)
}
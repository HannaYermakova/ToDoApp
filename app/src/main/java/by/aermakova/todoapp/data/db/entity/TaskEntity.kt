package by.aermakova.todoapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import by.aermakova.todoapp.data.db.database.TASKS_TABLE_NAME

@Entity(tableName = TASKS_TABLE_NAME)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val taskId: Long,
    val statusDone: Boolean,
    val text: String,
    val goalId: Long?,
    val keyResultId: Long?,
    val stepId: Long?,
    val finishTime: Long?,
    val startTime: Long,
    val scheduledTask: Boolean,
    val interval: Long?
)
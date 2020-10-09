package by.aermakova.todoapp.data.model

data class Task(
    val taskId: Long,
    val status: Boolean,
    val text: String,
    val goalId: Long?,
    val keyResultId: Long?,
    val stepId: Long?,
    val finishTime: Long,
    val startTime: Long,
    val scheduledTask: Boolean,
    val interval: Long
)
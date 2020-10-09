package by.aermakova.todoapp.data.model

data class Step(
    val stepId: Long,
    val keyResultId: Long,
    val goalId: Long,
    val status: Boolean,
    val text: String
)
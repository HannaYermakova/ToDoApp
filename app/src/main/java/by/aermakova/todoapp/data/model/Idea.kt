package by.aermakova.todoapp.data.model

data class Idea (
    val ideaId: Long,
    val goalId: Long,
    val keyResultId: Long?,
    val stepId: Long?,
    val text: String
)
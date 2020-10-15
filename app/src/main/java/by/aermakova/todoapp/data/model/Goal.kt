package by.aermakova.todoapp.data.model

import by.aermakova.todoapp.ui.adapter.Model

data class Goal(
    val goalId: Long,
    val status: Boolean,
    val text: String,
    val keyResults: List<Model<KeyResult>>
)
package by.aermakova.todoapp.data.model

import by.aermakova.todoapp.ui.adapter.ModelWrapper

data class Goal(
    val goalId: Long,
    val status: Boolean,
    val text: String,
    val keyResults: List<ModelWrapper<KeyResult>>
)
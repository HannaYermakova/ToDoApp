package by.aermakova.todoapp.data.model

import by.aermakova.todoapp.BR
import by.aermakova.todoapp.R

data class EmptyModel(
    val textId: Long = 0,
    val text: String = "",
) : CommonModel(textId, R.layout.item_empty_line, BR.empty)
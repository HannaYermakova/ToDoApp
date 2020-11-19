package by.aermakova.todoapp.data.useCase.actionEnum

import android.content.res.Resources
import by.aermakova.todoapp.data.model.FunctionLong
import by.aermakova.todoapp.data.model.TextModel


interface ActionTextConverter {
    fun toTextModel(
        res: Resources,
        clickAction: FunctionLong
    ): TextModel
}
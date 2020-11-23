package by.aermakova.todoapp.data.useCase.actionEnum

import android.content.res.Resources
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.model.FunctionLong
import by.aermakova.todoapp.data.model.TextModel

enum class TasksActionItem(private val listId: Int, val forDone: Boolean, private val imageId: Int? = null) :
    ActionTextConverter {
    EDIT_TASK(R.string.title_edit_task, false, R.drawable.ic_baseline_edit_24),
    DELETE_TASK(R.string.title_delete_task, true, R.drawable.ic_delete_24);

    override fun toTextModel(
        res: Resources,
        clickAction: FunctionLong
    ): TextModel {
        return TextModel(
            listId.toLong(),
            res.getString(listId),
            action = clickAction,
            imageId = imageId
        )
    }
}
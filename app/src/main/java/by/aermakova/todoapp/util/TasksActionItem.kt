package by.aermakova.todoapp.util

import android.content.res.Resources
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.model.FunctionLong
import by.aermakova.todoapp.data.model.TextModel

enum class TasksActionItem(private val listId: Int) : ActionTextConverter {
    EDIT_TASK(R.string.title_edit_task),
    DELETE_TASK(R.string.title_delete_task);

    override fun toTextModel(
        res: Resources,
        clickAction: FunctionLong
    ): TextModel {
        return TextModel(
            listId.toLong(),
            res.getString(listId),
            action = clickAction
        )
    }

}
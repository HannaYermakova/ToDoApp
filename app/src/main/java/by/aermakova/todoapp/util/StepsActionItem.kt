package by.aermakova.todoapp.util

import android.content.res.Resources
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.model.FunctionLong
import by.aermakova.todoapp.data.model.TextModel

enum class StepsActionItem(val listId: Int) : ActionTextConverter {
    ADD_TASK_TO_STEP(R.string.title_add_task),
    ADD_IDEA_TO_STEP(R.string.title_add_idea),
    EDIT_STEP(R.string.title_edit_step),
    DELETE_STEP(R.string.title_delete_step);

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

interface ActionTextConverter {
    fun toTextModel(
        res: Resources,
        clickAction: FunctionLong
    ): TextModel
}

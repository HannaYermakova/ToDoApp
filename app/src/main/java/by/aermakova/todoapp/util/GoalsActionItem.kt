package by.aermakova.todoapp.util

import android.content.res.Resources
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.model.FunctionLong
import by.aermakova.todoapp.data.model.TextModel

enum class GoalsActionItem(val listId: Int) : ActionTextConverter {
    ADD_KEY_RESULT_TO_GOAL(R.string.title_add_key_result),
    ADD_STEP_TO_GOAL(R.string.title_add_step),
    ADD_TASK_TO_GOAL(R.string.title_add_task),
    ADD_IDEA_TO_GOAL(R.string.title_add_idea),
    EDIT_GOAL(R.string.title_edit_goal),
    DELETE_GOAL(R.string.title_delete_goal);

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
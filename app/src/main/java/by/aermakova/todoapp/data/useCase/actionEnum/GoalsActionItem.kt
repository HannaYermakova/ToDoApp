package by.aermakova.todoapp.data.useCase.actionEnum

import android.content.res.Resources
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.model.FunctionLong
import by.aermakova.todoapp.data.model.TextModel
import by.aermakova.todoapp.data.useCase.actionEnum.ActionTextConverter

enum class GoalsActionItem(val listId: Int, private val imageId: Int? = null) :
    ActionTextConverter {
    ADD_KEY_RESULT_TO_GOAL(R.string.title_add_key_result),
    ADD_STEP_TO_GOAL(R.string.title_add_step),
    ADD_TASK_TO_GOAL(R.string.title_add_task),
    ADD_IDEA_TO_GOAL(R.string.title_add_idea),
    EDIT_GOAL(R.string.title_edit_goal, R.drawable.ic_baseline_edit_24),
    DELETE_GOAL(R.string.title_delete_goal, R.drawable.ic_delete_24);

    override fun toTextModel(
        res: Resources,
        clickAction: FunctionLong
    ): TextModel {
        return TextModel(
            textId = listId.toLong(),
            text = res.getString(listId),
            action = clickAction,
            imageId = imageId
        )
    }
}
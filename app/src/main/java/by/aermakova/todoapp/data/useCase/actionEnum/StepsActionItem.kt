package by.aermakova.todoapp.data.useCase.actionEnum

import android.content.res.Resources
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.model.FunctionLong
import by.aermakova.todoapp.data.model.TextModel

enum class StepsActionItem(private val listId: Int, private val forDoneAction: Boolean, private val imageId: Int? = null) :
    ActionTextConverter {
    ADD_TASK_TO_STEP(R.string.title_add_task, false),
    ADD_IDEA_TO_STEP(R.string.title_add_idea, false),
    EDIT_STEP(R.string.title_edit_step, false, R.drawable.ic_baseline_edit_24),
    DELETE_STEP(R.string.title_delete_step, true, R.drawable.ic_delete_24);

    override val forDone: Boolean
        get() = forDoneAction

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
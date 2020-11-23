package by.aermakova.todoapp.data.useCase.actionEnum

import android.content.res.Resources
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.model.FunctionLong
import by.aermakova.todoapp.data.model.TextModel

enum class IdeasActionItem(private val listId: Int, private val forDoneAction: Boolean, private val imageId: Int? = null) :
    ActionTextConverter {
    EDIT_IDEA(R.string.title_edit_idea, false, R.drawable.ic_baseline_edit_24),
    DELETE_IDEA(R.string.title_delete_idea, true, R.drawable.ic_delete_24);

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
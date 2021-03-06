package by.aermakova.todoapp.data.model

import by.aermakova.todoapp.BR
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.db.entity.IdeaEntity


data class IdeaModel(
    val ideaId: Long,
    val goalId: Long,
    val keyResultId: Long?,
    val stepId: Long?,
    val text: String,
    val action: FunctionLong? = null,
    val longClick: FunctionLong? = null
) : CommonModel(ideaId, R.layout.item_idea, BR.idea, action, longClick)

fun IdeaEntity.toCommonModel(clickAction: FunctionLong? = null, longClick: FunctionLong? = null): IdeaModel {
    return IdeaModel(ideaId, ideaGoalId, ideaKeyResultId, ideaStepId, text, clickAction, longClick)
}
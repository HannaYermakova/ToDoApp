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
    val action: Function? = null
) : CommonModel(ideaId, R.layout.item_idea, BR.idea, action)

fun IdeaEntity.toCommonModel(clickAction: Function? = null): IdeaModel {
    return IdeaModel(ideaId, ideaGoalId, ideaKeyResultId, ideaStepId, text, clickAction)
}
package by.aermakova.todoapp.data.model

import by.aermakova.todoapp.BR
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.db.entity.StepEntity

data class StepModel(
    val stepId: Long,
    val keyResultId: Long,
    val goalId: Long,
    val status: Boolean,
    val text: String,
    val stepItemsList: List<CommonModel>? = null,
    val action: FunctionLong? = null
) : CommonModel(stepId, R.layout.item_step, BR.step, action)

fun StepEntity.toCommonModel(
    innerObjects: List<CommonModel>? = null,
    clickAction: FunctionLong? = null
): StepModel {
    return StepModel(
        stepId,
        stepKeyResultId,
        stepGoalId,
        stepStatusDone,
        text,
        innerObjects,
        clickAction
    )
}
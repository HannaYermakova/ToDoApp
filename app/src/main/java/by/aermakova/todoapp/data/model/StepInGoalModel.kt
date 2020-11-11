package by.aermakova.todoapp.data.model

import by.aermakova.todoapp.BR
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.db.entity.StepEntity
import by.aermakova.todoapp.data.db.entity.StepTasks

data class StepInGoalModel(
    val stepId: Long,
    val keyResultId: Long,
    val goalId: Long,
    val status: Boolean,
    val text: String,
    val stepItemsList: List<CommonModel>? = null,
    val action: FunctionLong? = null
) : CommonModel(stepId, R.layout.item_step_in_goal, BR.step, action)

fun StepEntity.toCommonGoalModel(
    innerObjects: List<CommonModel>? = null,
    clickAction: FunctionLong
): StepInGoalModel {
    return StepInGoalModel(
        stepId,
        stepKeyResultId,
        stepGoalId,
        stepStatusDone,
        text,
        innerObjects,
        clickAction
    )
}

fun StepTasks.toCommonModel(innerObjects: List<CommonModel>? = null): StepInGoalModel {
    return with(step) {
        StepInGoalModel(
            stepId,
            stepKeyResultId,
            stepGoalId,
            stepStatusDone,
            text,
            innerObjects
        )
    }
}
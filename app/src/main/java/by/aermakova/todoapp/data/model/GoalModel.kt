package by.aermakova.todoapp.data.model

import by.aermakova.todoapp.BR
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.db.entity.GoalKeyResults

data class GoalModel(
    val goalId: Long,
    val status: Boolean,
    val text: String,
    val goalItemsList: List<CommonModel>? = null,
    val action: Function? = null
) : CommonModel(goalId, R.layout.item_goal, BR.goalModel, action)

fun GoalKeyResults.toCommonModel(
    goalItemsList: List<CommonModel>? = null,
    action: Function? = null
): GoalModel {
    return with(goal) {
        GoalModel(
            goalId,
            goalStatusDone,
            text,
            goalItemsList,
            action
        )
    }
}

fun List<GoalKeyResults>.toCommonModelGoalList(clickAction: Function): List<GoalModel> {
    return map {
        it.toCommonModel(action = clickAction)
    }
}
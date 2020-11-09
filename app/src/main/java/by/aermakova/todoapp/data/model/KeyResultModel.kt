package by.aermakova.todoapp.data.model

import by.aermakova.todoapp.BR
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.db.entity.KeyResultEntity
import by.aermakova.todoapp.data.db.entity.KeyResultTasks

data class KeyResultModel(
    val keyResultId: Long,
    val goalId: Long,
    val status: Boolean,
    val text: String,
    val keyResultItemsList: List<CommonModel>? = null,
    val action: FunctionSelect? = null
) : CommonModel(keyResultId, R.layout.item_key_result, BR.keyResult)

fun KeyResultTasks.toCommonModel(): KeyResultModel {
    return with(keyResult) {
        KeyResultModel(
            keyResultId,
            keyResultGoalId,
            keyResultStatusDone,
            text,
            tasks.map { it.toCommonModel {} })
    }
}

fun KeyResultEntity.toCommonModel(unattachedTasks: List<CommonModel>? = null, action: FunctionSelect? = null): KeyResultModel {
    return KeyResultModel(keyResultId, keyResultGoalId, keyResultStatusDone, text, unattachedTasks, action)
}
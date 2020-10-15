package by.aermakova.todoapp.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.aermakova.todoapp.BR
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.db.database.KEY_RESULTS_TABLE_NAME
import by.aermakova.todoapp.data.model.KeyResult
import by.aermakova.todoapp.ui.adapter.Model

@Entity(tableName = KEY_RESULTS_TABLE_NAME)
data class KeyResultEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "key_result_id")
    val keyResultId: Long = 0,

    @ColumnInfo(name = "key_result_goal_id")
    val keyResultGoalId: Long,

    @ColumnInfo(name = "key_result_status_done")
    val keyResultStatusDone: Boolean = false,

    val text: String
)

fun KeyResultEntity.toModel(): KeyResult {
    return KeyResult(keyResultId, keyResultGoalId, keyResultStatusDone, text)
}

fun KeyResult.toModelKeyResult(): Model<KeyResult> {
    return Model(keyResultId, this, R.layout.item_key_result, BR.keyResult)
}
package by.aermakova.todoapp.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.aermakova.todoapp.data.db.database.KEY_RESULTS_TABLE_NAME
import com.google.gson.annotations.SerializedName

@Entity(tableName = KEY_RESULTS_TABLE_NAME)
data class KeyResultEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="key_result_id")
    val keyResultId: Long,

    @ColumnInfo(name ="key_result_goal_id")
    val keyResultGoalId: Long,

    @ColumnInfo(name ="key_result_status_done")
    val keyResultStatusDone: Boolean,

    val text: String
)
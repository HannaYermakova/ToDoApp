package by.aermakova.todoapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import by.aermakova.todoapp.data.db.database.KEY_RESULTS_TABLE_NAME
import com.google.gson.annotations.SerializedName

@Entity(tableName = KEY_RESULTS_TABLE_NAME)
data class KeyResultEntity(

    @PrimaryKey(autoGenerate = true)
    @SerializedName("key_result_id")
    val keyResultId: Long,

    @SerializedName("goal_id")
    val goalId: Long,

    @SerializedName("status_done")
    val statusDone: Boolean,

    val text: String
)
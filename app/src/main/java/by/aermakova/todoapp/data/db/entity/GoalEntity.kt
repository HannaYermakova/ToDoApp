package by.aermakova.todoapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import by.aermakova.todoapp.data.db.database.GOALS_TABLE_NAME
import com.google.gson.annotations.SerializedName

@Entity(tableName = GOALS_TABLE_NAME)
data class GoalEntity(

    @PrimaryKey(autoGenerate = true)
    @SerializedName("goal_id")
    val goalId: Long,

    @SerializedName("status_done")
    val statusDone: Boolean,

    val text: String
)
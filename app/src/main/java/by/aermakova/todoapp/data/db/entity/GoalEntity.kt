package by.aermakova.todoapp.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.aermakova.todoapp.data.db.database.GOALS_TABLE_NAME
import com.google.gson.annotations.SerializedName

@Entity(tableName = GOALS_TABLE_NAME)
data class GoalEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "goal_id")
    val goalId: Long = 0,

    @ColumnInfo(name ="goal_status_done")
    val goalStatusDone: Boolean,

    val text: String
)
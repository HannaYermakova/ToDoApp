package by.aermakova.todoapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import by.aermakova.todoapp.data.db.database.GOALS_TABLE_NAME

@Entity(tableName = GOALS_TABLE_NAME)
data class GoalEntity(

    @PrimaryKey(autoGenerate = true)
    val goalId: Long,
    val statusDone: Boolean,
    val text: String
)
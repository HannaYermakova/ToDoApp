package by.aermakova.todoapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import by.aermakova.todoapp.data.db.database.STEPS_TABLE_NAME

@Entity(tableName = STEPS_TABLE_NAME)
data class StepEntity(

    @PrimaryKey
    val stepId: Long,
    val keyResultId: Long,
    val goalId: Long,
    val statusDone: Boolean,
    val text: String
)
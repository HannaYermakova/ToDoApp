package by.aermakova.todoapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import by.aermakova.todoapp.data.db.database.IDEAS_TABLE_NAME

@Entity(tableName = IDEAS_TABLE_NAME)
data class IdeaEntity(

    @PrimaryKey
    val ideaId: Long,
    val goalId: Long?,
    val keyResultId: Long?,
    val stepId: Long?,
    val text: String
)
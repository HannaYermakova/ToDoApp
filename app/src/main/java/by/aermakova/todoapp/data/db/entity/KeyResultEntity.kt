package by.aermakova.todoapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import by.aermakova.todoapp.data.db.database.KEY_RESULTS_TABLE_NAME

@Entity(tableName = KEY_RESULTS_TABLE_NAME)
data class KeyResultEntity(

    @PrimaryKey(autoGenerate = true)
    val keyResultId: Long,
    val goalId: Long,
    val status: Boolean,
    val text: String
)
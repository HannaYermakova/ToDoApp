package by.aermakova.todoapp.data.db.entity

import androidx.room.*
import by.aermakova.todoapp.data.db.database.GOALS_TABLE_NAME
import by.aermakova.todoapp.data.model.Goal
import by.aermakova.todoapp.ui.adapter.toModelKeyResult

@Entity(tableName = GOALS_TABLE_NAME)
data class GoalEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "goal_id")
    val goalId: Long = 0,

    @ColumnInfo(name = "goal_status_done")
    val goalStatusDone: Boolean,

    val text: String
)

data class GoalKeyResults(
    @Embedded val goal: GoalEntity,
    @Relation(
        parentColumn = "goal_id",
        entityColumn = "key_result_goal_id"
    )
    val keyResults: List<KeyResultEntity>
)

fun GoalKeyResults.toModel(): Goal {
    return with(goal) { Goal(goalId, goalStatusDone, text, keyResults.map { it.toModel().toModelKeyResult() }) }
}
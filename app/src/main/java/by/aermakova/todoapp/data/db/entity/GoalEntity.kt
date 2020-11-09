package by.aermakova.todoapp.data.db.entity

import androidx.room.*
import by.aermakova.todoapp.data.db.database.GOALS_TABLE_NAME

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

data class KeyResultTasks(
    @Embedded val keyResult: KeyResultEntity,
    @Relation(
        parentColumn = "key_result_id",
        entityColumn = "task_key_result_id"
    )
    val tasks: List<TaskEntity>
)

data class KeyResultSteps(
    @Embedded val keyResult: KeyResultEntity,
    @Relation(
        parentColumn = "key_result_id",
        entityColumn = "step_key_result_id"
    )
    val steps: List<StepEntity>
)

data class StepTasks(
    @Embedded val step: StepEntity,
    @Relation(
        parentColumn = "step_id",
        entityColumn = "task_step_id"
    )
    val tasks: List<TaskEntity>
)
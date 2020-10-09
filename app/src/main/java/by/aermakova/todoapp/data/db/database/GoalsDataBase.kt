package by.aermakova.todoapp.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import by.aermakova.todoapp.data.db.dao.*
import by.aermakova.todoapp.data.db.entity.*

const val GOALS_DATABASE_NAME = "Goals.db"
const val GOALS_TABLE_NAME = "goals_table"
const val TASKS_TABLE_NAME = "tasks_table"
const val IDEAS_TABLE_NAME = "ideas_table"
const val KEY_RESULTS_TABLE_NAME = "key_results_table"
const val STEPS_TABLE_NAME = "steps_table"

@Database(
    entities = [TaskEntity::class,
        IdeaEntity::class,
        KeyResultEntity::class,
        StepEntity::class,
        GoalEntity::class
    ], version = 1, exportSchema = false
)
abstract class GoalsDataBase : RoomDatabase() {

    abstract fun getTaskDao(): TaskDao
    abstract fun getGoalDao(): GoalDao
    abstract fun getIdeaDao(): IdeaDao
    abstract fun getStepDao(): StepDao
    abstract fun getKeyResultDao(): KeyResultDao
}
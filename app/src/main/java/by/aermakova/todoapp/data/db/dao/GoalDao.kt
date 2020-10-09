package by.aermakova.todoapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import by.aermakova.todoapp.data.db.entity.GoalEntity
import io.reactivex.Flowable

@Dao
interface GoalDao {

    @Insert
    fun insertGoal(goal: GoalEntity)

    @Insert
    fun insertAllGoals(goals: List<GoalEntity>)

    @Query("SELECT * FROM goals_table")
    fun getAllGoals(): Flowable<List<GoalEntity>>

    @Query("SELECT * FROM goals_table WHERE goalId =:goalId")
    fun getGoalById(goalId: Long): Flowable<GoalEntity>
}
package by.aermakova.todoapp.data.db.dao

import androidx.room.*
import by.aermakova.todoapp.data.db.entity.GoalEntity
import by.aermakova.todoapp.data.db.entity.GoalKeyResults
import io.reactivex.Observable

@Dao
interface GoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGoal(goal: GoalEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllGoals(goals: List<GoalEntity>)

    @Query("SELECT * FROM goals_table")
    fun getAllGoals(): Observable<List<GoalEntity>>

    @Query("SELECT * FROM goals_table WHERE goal_id =:goalId")
    fun getGoalById(goalId: Long): GoalEntity

    @Transaction
    @Query("SELECT * FROM goals_table")
    fun getAllGoalsWithKeyResults(): Observable<List<GoalKeyResults>>

    @Transaction
    @Query("SELECT * FROM goals_table WHERE goal_id =:id")
    fun getGoalWithKeyResultsById(id: Long): Observable<GoalKeyResults>
}
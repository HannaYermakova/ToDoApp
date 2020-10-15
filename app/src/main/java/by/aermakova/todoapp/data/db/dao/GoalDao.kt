package by.aermakova.todoapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import by.aermakova.todoapp.data.db.entity.GoalEntity
import by.aermakova.todoapp.data.db.entity.GoalKeyResults
import io.reactivex.Observable

@Dao
interface GoalDao {

    @Insert
    fun insertGoal(goal: GoalEntity): Long

    @Insert
    fun insertAllGoals(goals: List<GoalEntity>)

    @Query("SELECT * FROM goals_table")
    fun getAllGoals(): Observable<List<GoalEntity>>

    @Query("SELECT * FROM goals_table WHERE goal_id =:goalId")
    fun getGoalById(goalId: Long): Observable<GoalEntity>

    @Transaction
    @Query("SELECT * FROM goals_table")
    fun getAllGoalsWithKeyResults(): Observable<List<GoalKeyResults>>

    @Transaction
    @Query("SELECT * FROM goals_table WHERE goal_id =:id")
    fun getGoalWithKeyResultsById(id: Long): Observable<GoalKeyResults>
}
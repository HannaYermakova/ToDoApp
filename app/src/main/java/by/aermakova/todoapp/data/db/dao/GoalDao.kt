package by.aermakova.todoapp.data.db.dao

import androidx.room.*
import by.aermakova.todoapp.data.db.entity.*
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

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

    @Query("SELECT * FROM goals_table WHERE goal_id =:goalId")
    fun getObsGoalById(goalId: Long): Observable<GoalEntity>

    @Transaction
    @Query("SELECT * FROM goals_table")
    fun getAllGoalsWithKeyResults(): Observable<List<GoalKeyResults>>

    @Transaction
    @Query("SELECT * FROM goals_table WHERE goal_id =:id")
    fun getGoalWithKeyResultsById(id: Long): Single<GoalKeyResults>

    @Transaction
    @Query("SELECT * FROM key_results_table WHERE key_result_id =:keyRes ")
    fun getKeyResultWithStepsById(keyRes: Long): Single<KeyResultSteps>

    @Query("UPDATE goals_table SET goal_status_done = :status WHERE goal_id = :goalId")
    fun updateGoal(status: Boolean, goalId: Long)

    @Query("UPDATE tasks_table SET task_status_done = :status WHERE task_goal_id = :goalId")
    fun updateTasksInGoal(status: Boolean, goalId: Long)

    @Query("UPDATE tasks_table SET task_status_done = :status WHERE task_key_result_id  IN (:keyResultIds)")
    fun updateTasksInKeyResult(status: Boolean, keyResultIds: List<Long>)

    @Query("UPDATE key_results_table SET key_result_status_done = :status WHERE key_result_goal_id = :goalId")
    fun updateKeyResultsInGoal(status: Boolean, goalId: Long)

    @Query("UPDATE key_results_table SET key_result_status_done = :status WHERE key_result_id IN (:keyResultIds)")
    fun updateKeyResults(status: Boolean, keyResultIds: List<Long>)

    @Query("UPDATE steps_table SET step_status_done = :status WHERE step_goal_id = :goalId")
    fun updateStepsInGoal(status: Boolean, goalId: Long)

    @Query("UPDATE steps_table SET step_status_done = :status WHERE step_key_result_id IN (:keyResultIds)")
    fun updateStepsInKeyResult(status: Boolean, keyResultIds: List<Long>)

    @Query("SELECT * FROM key_results_table WHERE key_result_goal_id =:goalId ")
    fun getKeyResultByGoalId(goalId: Long): Single<List<KeyResultEntity>>

    @Transaction
    fun updateAllGoalItems(status: Boolean, goalId: Long) {
        updateGoal(status, goalId)
        updateTasksInGoal(status, goalId)
        updateKeyResultsInGoal(status, goalId)
        updateStepsInGoal(status, goalId)
    }

    @Transaction
    fun updateKeyResultsInGoal(status: Boolean, keyResultIds: List<Long>) {
        updateKeyResults(status, keyResultIds)
        updateTasksInKeyResult(status, keyResultIds)
        updateStepsInKeyResult(status, keyResultIds)
    }

    @Query("SELECT * FROM key_results_table WHERE key_result_id IN (:keyResultIds)")
    fun getKeyResultsByIds(keyResultIds: List<Long>): Single<List<KeyResultEntity>>

    @Query("DELETE FROM goals_table")
    fun deleteAllGoals(): Single<Int>

    @Query("DELETE FROM key_results_table")
    fun deleteAllKeyResults(): Single<Int>

    @Query("DELETE FROM steps_table")
    fun deleteAllSteps(): Single<Int>

    @Query("DELETE FROM ideas_table")
    fun deleteAllIdeas(): Single<Int>

    @Query("DELETE FROM tasks_table")
    fun deleteAllTasks(): Single<Int>

    fun deleteAll(): Flowable<Int> {
        return Single.merge(
            deleteAllGoals(),
            deleteAllSteps(),
            deleteAllTasks(),
            deleteAllIdeas())
    }
}
package by.aermakova.todoapp.data.db.dao

import androidx.room.*
import by.aermakova.todoapp.data.db.entity.GoalEntity
import by.aermakova.todoapp.data.db.entity.GoalKeyResults
import by.aermakova.todoapp.data.db.entity.KeyResultEntity
import by.aermakova.todoapp.data.db.entity.KeyResultSteps
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

    @Query("SELECT * FROM goals_table WHERE goal_status_done = 0")
    fun getAllUndoneGoals(): Observable<List<GoalEntity>>

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
    fun updateGoalStatus(status: Boolean, goalId: Long)

    @Query("UPDATE goals_table SET text = :newGoalText WHERE goal_id = :goalId")
    fun updateGoalText(newGoalText: String, goalId: Long) : Int

    @Query("UPDATE tasks_table SET task_status_done = :status WHERE task_goal_id = :goalId")
    fun updateTasksInGoalStatus(status: Boolean, goalId: Long)

    @Query("UPDATE tasks_table SET task_status_done = :status WHERE task_key_result_id  IN (:keyResultIds)")
    fun updateTasksInKeyResultStatus(status: Boolean, keyResultIds: List<Long>)

    @Query("UPDATE key_results_table SET key_result_status_done = :status WHERE key_result_goal_id = :goalId")
    fun updateKeyResultsStatusInGoal(status: Boolean, goalId: Long)

    @Query("UPDATE key_results_table SET key_result_status_done = :status WHERE key_result_id IN (:keyResultIds)")
    fun updateKeyResultsStatus(status: Boolean, keyResultIds: List<Long>)

    @Query("UPDATE steps_table SET step_status_done = :status WHERE step_goal_id = :goalId")
    fun updateStepsInGoalStatus(status: Boolean, goalId: Long)

    @Query("UPDATE steps_table SET step_status_done = :status WHERE step_key_result_id IN (:keyResultIds)")
    fun updateStepsInKeyResultStatus(status: Boolean, keyResultIds: List<Long>)

    @Query("SELECT * FROM key_results_table WHERE key_result_goal_id =:goalId ")
    fun getKeyResultByGoalId(goalId: Long): Single<List<KeyResultEntity>>

    @Transaction
    fun updateAllGoalItemsStatus(status: Boolean, goalId: Long) {
        updateGoalStatus(status, goalId)
        updateTasksInGoalStatus(status, goalId)
        updateKeyResultsStatusInGoal(status, goalId)
        updateStepsInGoalStatus(status, goalId)
    }

    @Transaction
    fun updateKeyResultsInGoal(status: Boolean, keyResultIds: List<Long>) {
        updateKeyResultsStatus(status, keyResultIds)
        updateTasksInKeyResultStatus(status, keyResultIds)
        updateStepsInKeyResultStatus(status, keyResultIds)
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
            deleteAllIdeas()
        )
    }

    @Query("SELECT goal_status_done FROM goals_table WHERE goal_id =:goalId")
    fun checkGoalDone(goalId: Long): Single<Boolean>

    @Query("DELETE FROM goals_table WHERE goal_id =:goalId")
    fun deleteGoalById(goalId: Long)

    @Query("SELECT key_result_id FROM key_results_table WHERE key_result_goal_id =:goalId")
    fun getAllKeyResultsIdByGoalId(goalId: Long): Single<List<Long>>
}
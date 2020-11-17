package by.aermakova.todoapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.aermakova.todoapp.data.db.entity.TaskEntity
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: TaskEntity): Long

    @Query("UPDATE tasks_table SET task_status_done = :status WHERE task_id = :taskId")
    fun updateTask(status: Boolean, taskId: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTasks(tasks: List<TaskEntity>)

    @Query("SELECT * FROM tasks_table WHERE task_id = :taskId")
    fun getTaskById(taskId: Long): Single<TaskEntity>

    @Query("SELECT * FROM tasks_table WHERE task_step_id = :stepId")
    fun getTasksByStepId(stepId: Long): Single<List<TaskEntity>>

    @Query("SELECT * FROM tasks_table WHERE task_goal_id = :goalId")
    fun getTasksByGoalId(goalId: Long): Single<List<TaskEntity>>

    @Query("SELECT * FROM tasks_table")
    fun getAllTasks(): Observable<List<TaskEntity>>

    @Query("SELECT * FROM tasks_table WHERE task_goal_id = :goalId AND task_key_result_id IS NULL")
    fun getTasksUnattachedToKeyResult(goalId: Long): Single<List<TaskEntity>>

    @Query("SELECT * FROM tasks_table WHERE task_key_result_id = :keyResultId AND task_step_id IS NULL")
    fun getTasksUnattachedToStep(keyResultId: Long): Single<List<TaskEntity>>

    @Query("SELECT * FROM tasks_table WHERE task_key_result_id IN (:keyResIds)")
    fun getTasksByKeyResultsIds(keyResIds: List<Long>): Single<List<TaskEntity>>

    @Query("DELETE FROM tasks_table WHERE task_goal_id =:goalId")
    fun deleteTaskByGoalId(goalId: Long)

    @Query("SELECT task_id FROM tasks_table WHERE task_goal_id = :goalId")
    fun getAllTasksIdByGoalId(goalId: Long): Single<List<Long>>

    @Query("SELECT task_id FROM tasks_table WHERE task_step_id = :stepId")
    fun getAllTasksIdByStepId(stepId: Long): Single<List<Long>>

    @Query("DELETE FROM tasks_table WHERE task_step_id =:stepId")
    fun deleteTaskByStepId(stepId: Long)

    @Query("DELETE FROM tasks_table WHERE task_id =:taskId")
    fun deleteTaskById(taskId: Long):Int
}
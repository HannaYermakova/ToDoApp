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

    @Query("SELECT * FROM tasks_table")
    fun getAllTasks(): Observable<List<TaskEntity>>
}
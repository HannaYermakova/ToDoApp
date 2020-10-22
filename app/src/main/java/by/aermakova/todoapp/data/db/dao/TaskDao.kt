package by.aermakova.todoapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.aermakova.todoapp.data.db.entity.TaskEntity
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: TaskEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTasks(tasks: List<TaskEntity>)

    @Query("SELECT * FROM tasks_table WHERE task_id = :taskId")
    fun getTaskById(taskId: Long): Observable<TaskEntity>

    @Query("SELECT * FROM tasks_table")
    fun getAllTasks(): Observable<List<TaskEntity>>
}
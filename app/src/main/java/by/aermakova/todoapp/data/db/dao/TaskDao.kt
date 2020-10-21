package by.aermakova.todoapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import by.aermakova.todoapp.data.db.entity.TaskEntity
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface TaskDao {

    @Insert
    fun insertTask(task: TaskEntity): Long

    @Insert
    fun insertAllTasks(tasks: List<TaskEntity>)

    @Query("SELECT * FROM tasks_table WHERE task_id = :taskId")
    fun getTaskById(taskId: Long): Observable<TaskEntity>

    @Query("SELECT * FROM tasks_table")
    fun getAllTasks(): Flowable<List<TaskEntity>>
}
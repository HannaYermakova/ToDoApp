package by.aermakova.todoapp.data.repository

import by.aermakova.todoapp.data.db.dao.TaskDao
import by.aermakova.todoapp.data.db.entity.TaskEntity
import io.reactivex.Observable
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {

    fun saveTask(taskEntity: TaskEntity) : Long = taskDao.insertTask(taskEntity)

    fun getTaskById(taskId: Long): Observable<TaskEntity> =
        taskDao.getTaskById(taskId)

    fun getAllTasks(): Observable<List<TaskEntity>> {
        return taskDao.getAllTasks()
    }
}
package by.aermakova.todoapp.data.repository

import by.aermakova.todoapp.data.db.dao.TaskDao
import by.aermakova.todoapp.data.db.entity.TaskEntity
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {

    fun saveTask(taskEntity: TaskEntity) : Long = taskDao.insertTask(taskEntity)

    fun getTaskById(taskId: Long): Single<TaskEntity> =
        taskDao.getTaskById(taskId)

    fun getAllTasks(): Observable<List<TaskEntity>> {
        return taskDao.getAllTasks()
    }

    fun updateTask(status: Boolean, taskId: Long) {
        taskDao.updateTask(status, taskId)
    }
}
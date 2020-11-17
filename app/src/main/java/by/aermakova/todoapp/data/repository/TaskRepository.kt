package by.aermakova.todoapp.data.repository

import by.aermakova.todoapp.data.db.dao.TaskDao
import by.aermakova.todoapp.data.db.entity.TaskEntity
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {

    fun saveTask(taskEntity: TaskEntity): Long = taskDao.insertTask(taskEntity)

    fun getTaskById(taskId: Long): Single<TaskEntity> = taskDao.getTaskById(taskId)

    fun getAllTasks(): Observable<List<TaskEntity>> {
        return taskDao.getAllTasks()
    }

    fun updateTask(status: Boolean, taskId: Long) {
        taskDao.updateTask(status, taskId)
    }

    fun getTaskByStepId(stepId: Long): Single<List<TaskEntity>> {
        return taskDao.getTasksByStepId(stepId)
    }

    fun getTasksByGoalId(goalId: Long): Single<List<TaskEntity>> {
        return taskDao.getTasksByGoalId(goalId)
    }

    fun getTasksByKeyResultIds(keyResIds: List<Long>): Single<List<TaskEntity>> {
        return taskDao.getTasksByKeyResultsIds(keyResIds)
    }

    fun saveTasks(taskEntities: List<TaskEntity>) {
        taskDao.insertAllTasks(taskEntities)
    }

    fun getAllTasksIdByGoalId(goalId: Long): Single<List<Long>> {
        return taskDao.getAllTasksIdByGoalId(goalId)
    }

    fun getAllTasksIdByStepId(stepId: Long): Single<List<Long>> {
        return taskDao.getAllTasksIdByStepId(stepId)
    }

    fun deleteTask(taskId: Long): Boolean {
        return taskDao.deleteTaskById(taskId) > 0
    }

    fun updateTaskText(newText: String, itemId: Long) =
        taskDao.updateTaskText(newText, itemId) > 0
}
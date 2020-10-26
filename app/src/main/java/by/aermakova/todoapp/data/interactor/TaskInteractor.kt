package by.aermakova.todoapp.data.interactor

import by.aermakova.todoapp.data.db.entity.Interval
import by.aermakova.todoapp.data.db.entity.TaskEntity
import by.aermakova.todoapp.data.remote.RemoteDatabase
import by.aermakova.todoapp.data.remote.model.TaskRemoteModel
import by.aermakova.todoapp.data.remote.model.toRemote
import by.aermakova.todoapp.data.repository.TaskRepository
import by.aermakova.todoapp.util.TaskFilterItem
import io.reactivex.Observable

class TaskInteractor(
    private val taskRepository: TaskRepository,
    private val taskRemoteDatabase: RemoteDatabase<TaskRemoteModel>
) {

    fun saveTaskInLocalDatabase(
        text: String,
        goalId: Long?,
        keyResultId: Long?,
        stepId: Long?,
        finishDate: Long?,
        scheduledTask: Boolean,
        interval: Interval?
    ): Long {
        return taskRepository.saveTask(
            TaskEntity(
                text = text,
                taskGoalId = goalId,
                taskKeyResultId = keyResultId,
                taskStepId = stepId,
                finishDate = finishDate,
                scheduledTask = scheduledTask,
                interval = interval?.code
            )
        )
    }

    fun getTaskById(taskId: Long): Observable<TaskEntity> = taskRepository.getTaskById(taskId)

    fun saveTaskToRemote(taskEntity: TaskEntity?) {
        taskEntity?.let {
            taskRemoteDatabase.saveData(it.toRemote())
        }
    }

    fun getAllTasks(): Observable<List<TaskEntity>> {
        return taskRepository.getAllTasks()
    }

    fun getFilterItems(): List<TaskFilterItem> {
        val list = arrayListOf<TaskFilterItem>()
        for (item in enumValues<TaskFilterItem>()) {
            list.add(item)
        }
        return list
    }
}
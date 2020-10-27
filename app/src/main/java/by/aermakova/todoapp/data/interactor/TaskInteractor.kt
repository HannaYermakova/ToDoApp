package by.aermakova.todoapp.data.interactor

import android.util.Log
import by.aermakova.todoapp.data.db.entity.Interval
import by.aermakova.todoapp.data.db.entity.TaskEntity
import by.aermakova.todoapp.data.remote.RemoteDatabase
import by.aermakova.todoapp.data.remote.model.TaskRemoteModel
import by.aermakova.todoapp.data.remote.model.toRemote
import by.aermakova.todoapp.data.repository.TaskRepository
import by.aermakova.todoapp.ui.adapter.TextModel
import by.aermakova.todoapp.ui.adapter.toTextModel
import by.aermakova.todoapp.util.TaskFilterItem
import by.aermakova.todoapp.util.TaskSortItem
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable


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

    fun getTaskById(taskId: Long): Single<TaskEntity> = taskRepository.getTaskById(taskId)

    fun saveTaskToRemote(taskEntity: TaskEntity?) {
        taskEntity?.let {
            taskRemoteDatabase.saveData(it.toRemote())
        }
    }

    fun getAllTasks(): Observable<List<TaskEntity>> {
        return taskRepository.getAllTasks()
    }

    fun getFilterItems(): List<TaskFilterItem> {
        return TaskFilterItem.values().asList()
    }

    fun getSortItems(): List<TaskSortItem> {
        return TaskSortItem.values().asList()
    }

    fun updateTask(status: Boolean, taskId: Long) {
        taskRepository.updateTask(status, taskId)
    }

    fun updateTaskToRemote(taskEntity: TaskEntity?) {
        taskEntity?.let {
            taskRemoteDatabase.updateData(it.toRemote())
        }
    }

    fun getTaskByStepId(stepId: Long): Single<List<TextModel>> {
        return taskRepository.getTaskByStepId(stepId).map { list -> list.map { it.toTextModel() } }
    }

    fun markStepsTasksAsDone(status: Boolean, stepId: Long): Disposable {
        return taskRepository.getTaskByStepId(stepId).subscribe({ list ->
            list.forEach {
                Log.d("A_TaskInteractor", "$it")
                updateTask(status, it.taskId)
                updateTaskToRemote(it)
            }
        },
            { it.printStackTrace() }
        )
    }
}
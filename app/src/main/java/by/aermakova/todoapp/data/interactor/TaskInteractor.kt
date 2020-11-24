package by.aermakova.todoapp.data.interactor

import by.aermakova.todoapp.data.db.entity.Interval
import by.aermakova.todoapp.data.db.entity.TaskEntity
import by.aermakova.todoapp.data.remote.DeleteGoalItems
import by.aermakova.todoapp.data.remote.DeleteStepItems
import by.aermakova.todoapp.data.remote.RemoteDatabase
import by.aermakova.todoapp.data.remote.model.TaskRemoteModel
import by.aermakova.todoapp.data.remote.model.toLocal
import by.aermakova.todoapp.data.remote.model.toRemote
import by.aermakova.todoapp.data.remote.sync.RemoteSync
import by.aermakova.todoapp.data.repository.TaskRepository
import by.aermakova.todoapp.data.useCase.actionEnum.TaskFilterItem
import by.aermakova.todoapp.data.useCase.actionEnum.TaskSortItem
import io.reactivex.Observer
import io.reactivex.Single

class TaskInteractor(
    private val taskRepository: TaskRepository,
    private val taskRemoteDatabase: RemoteDatabase<TaskRemoteModel>
) : RemoteSync<TaskRemoteModel>, DeleteGoalItems, DeleteStepItems {

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

    fun updateTaskInLocalDatabase(
        taskId: Long,
        text: String,
        goalId: Long?,
        keyResultId: Long?,
        stepId: Long?,
        startDate: Long,
        finishDate: Long?,
        scheduledTask: Boolean,
        interval: Int?
    ): Long {
        return taskRepository.saveTask(
            TaskEntity(
                taskId = taskId,
                text = text,
                taskGoalId = goalId,
                taskKeyResultId = keyResultId,
                taskStepId = stepId,
                startTime = startDate,
                finishDate = finishDate,
                scheduledTask = scheduledTask,
                interval = interval
            )
        )
    }

    fun getTaskById(taskId: Long): Single<TaskEntity> = taskRepository.getTaskById(taskId)

    fun saveTaskToRemote(taskEntity: TaskEntity?) {
        taskEntity?.let {
            taskRemoteDatabase.saveData(it.toRemote())
        }
    }

    fun getAllTasks() = taskRepository.getAllTasks()

    fun getFilterItems() = TaskFilterItem.values().asList()

    fun getSortItems() = TaskSortItem.values()

    fun updateTask(status: Boolean, taskId: Long) {
        taskRepository.updateTask(status, taskId)
    }

    fun updateTaskToRemote(taskEntity: TaskEntity?) {
        taskEntity?.let {
            taskRemoteDatabase.updateData(it.toRemote())
        }
    }

    fun getTaskByStepId(stepId: Long) = taskRepository.getTaskByStepId(stepId)

    fun markStepsTasksAsDone(status: Boolean, stepId: Long) =
        taskRepository.getTaskByStepId(stepId).subscribe({ list ->
            list.forEach {
                updateTask(status, it.taskId)
                updateTaskToRemote(it)
            }
        },
            { it.printStackTrace() }
        )

    override fun addItemsDataListener(dataObserver: Observer<List<TaskRemoteModel>>) {
        taskRemoteDatabase.addDataListener(dataObserver)
    }

    override fun saveItemsInLocalDatabase(list: List<TaskRemoteModel>) {
        taskRepository.saveTasks(list.map { it.toLocal() })
    }

    override fun deleteGoalsItemsById(goalId: Long) =
        taskRepository.getAllTasksIdByGoalId(goalId).map { ids ->
            ids.map { taskRemoteDatabase.removeData(it) }
        }

    override fun deleteStepItemsById(stepId: Long) =
        taskRepository.getAllTasksIdByStepId(stepId).map { ids ->
            ids.map { taskRemoteDatabase.removeData(it) }
        }

    fun deleteTaskByIdRemote(taskId: Long): Single<Boolean> {
        taskRemoteDatabase.removeData(taskId)
        return Single.just(true)
    }

    fun deleteTaskByIdLocal(taskId: Long) = Single.just(taskRepository.deleteTask(taskId))
}
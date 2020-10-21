package by.aermakova.todoapp.data.interactor

import by.aermakova.todoapp.data.db.entity.TaskEntity
import by.aermakova.todoapp.data.remote.RemoteDatabase
import by.aermakova.todoapp.data.remote.model.TaskRemoteModel
import by.aermakova.todoapp.data.remote.model.toRemote
import by.aermakova.todoapp.data.repository.TaskRepository
import io.reactivex.Observable

class TaskInteractor(
    private val taskRepository: TaskRepository,
    private val taskRemoteDatabase: RemoteDatabase<TaskRemoteModel>
) {

    fun saveTaskInLocalDatabase(text: String, goalId: Long?, keyResultId: Long?, stepId: Long?) : Long {
        return taskRepository.saveTask(
            TaskEntity(
                text = text,
                taskGoalId = goalId,
                taskKeyResultId = keyResultId,
                taskStepId = stepId,

                finishTime = 0L,
                scheduledTask = false,
                interval = null
            )
        )
    }

    fun getTaskById(taskId: Long): Observable<TaskEntity>  = taskRepository.getTaskById(taskId)

    fun saveTaskToRemote(taskEntity: TaskEntity?) {
        taskEntity?.let {
            taskRemoteDatabase.saveData(it.toRemote())
        }
    }
}
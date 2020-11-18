package by.aermakova.todoapp.ui.task.edit

import by.aermakova.todoapp.data.db.entity.TaskEntity
import by.aermakova.todoapp.data.di.scope.NavigationSteps
import by.aermakova.todoapp.data.useCase.EditTaskUseCase
import by.aermakova.todoapp.data.useCase.SetTaskFieldsUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.task.TasksNavigation
import javax.inject.Inject

class EditTaskViewModel @Inject constructor(
    val setTaskFieldsUseCase: SetTaskFieldsUseCase,
    val editTaskUseCase: EditTaskUseCase,
    @NavigationSteps private val mainFlowNavigation: TasksNavigation,
    taskId: Long
) : BaseViewModel() {

    private lateinit var taskEntity: TaskEntity

    val popBack = { mainFlowNavigation.popBack() }

    val saveTask = {
        editTaskUseCase.saveUpdatedTaskLocal(
            disposable,
            taskEntity,
            loadingAction,
            { mainFlowNavigation.popBack() },
            errorAction
        )
    }

    init {
        setTaskFieldsUseCase.setTaskFields(taskId, disposable, { taskEntity = it }, errorAction)
    }

}
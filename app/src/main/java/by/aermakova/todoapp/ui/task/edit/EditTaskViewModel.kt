package by.aermakova.todoapp.ui.task.edit

import by.aermakova.todoapp.data.db.entity.TaskEntity
import by.aermakova.todoapp.data.di.scope.NavigationSteps
import by.aermakova.todoapp.data.useCase.EditTaskUseCase
import by.aermakova.todoapp.data.useCase.SetTaskFieldsUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.task.TasksNavigation
import javax.inject.Inject


class EditTaskViewModel @Inject constructor(
    val setTaskFieldsUseCase: SetTaskFieldsUseCase,
    val editTaskUseCase: EditTaskUseCase,
    @NavigationSteps private val navigation: TasksNavigation,
    taskId: Long
) : BaseViewModel() {

    override val mainFlowNavigation: MainFlowNavigation
        get() = navigation

    private lateinit var taskEntity: TaskEntity

    val saveTask = {
        editTaskUseCase.saveUpdatedTaskLocal(
            disposable,
            taskEntity,
            loadingAction,
            { popBack.invoke() },
            errorAction
        )
    }

    init {
        setTaskFieldsUseCase.setTaskFields(taskId, disposable, { taskEntity = it }, errorAction)
    }
}
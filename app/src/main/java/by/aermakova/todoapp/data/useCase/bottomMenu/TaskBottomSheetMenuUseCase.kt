package by.aermakova.todoapp.data.useCase.bottomMenu

import android.content.res.Resources
import android.view.View
import by.aermakova.todoapp.data.db.entity.TaskEntity
import by.aermakova.todoapp.data.model.FunctionString
import by.aermakova.todoapp.data.useCase.delete.DeleteTaskUseCase
import by.aermakova.todoapp.data.useCase.FindTaskUseCase
import by.aermakova.todoapp.data.useCase.actionEnum.TasksActionItem
import by.aermakova.todoapp.databinding.BottomSheetTaskActionBinding
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.task.main.TasksViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.disposables.CompositeDisposable


class TaskBottomSheetMenuUseCase(
    val deleteTaskUseCase: DeleteTaskUseCase,
    private val taskActionBind: BottomSheetTaskActionBinding,
    private val mainFlowNavigation: MainFlowNavigation,
    private val findTaskUseCase: FindTaskUseCase,
    dialog: BottomSheetDialog,
    taskActionItems: Array<TasksActionItem>,
    resources: Resources
) : CommonBottomSheetMenuUseCase<TasksViewModel, TaskEntity, DeleteTaskUseCase, TasksActionItem>(
    deleteTaskUseCase, dialog, taskActionItems, resources
) {

    override fun bindViewModel(viewModel: TasksViewModel) {
        taskActionBind.viewModel = viewModel
    }

    override val rootView: View
        get() = taskActionBind.root

    override fun useItemById(
        disposable: CompositeDisposable,
        itemId: Long,
        function: (TaskEntity) -> Unit,
        errorAction: FunctionString
    ) {
        findTaskUseCase.useTasksById(disposable, itemId, function, errorAction)
    }

    override fun checkIsItemDone(entity: TaskEntity) = entity.taskStatusDone

    override fun setItemAction(
        action: TasksActionItem,
        disposable: CompositeDisposable,
        errorAction: FunctionString
    ) {
        when (action) {
            TasksActionItem.EDIT_TASK -> mainFlowNavigation.navigateToEditElementFragment(
                selectedItemId
            )
            TasksActionItem.DELETE_TASK -> deleteTaskUseCase.confirmDeleteItem(
                selectedItemId,
                disposable,
                errorAction
            )
        }
    }
}
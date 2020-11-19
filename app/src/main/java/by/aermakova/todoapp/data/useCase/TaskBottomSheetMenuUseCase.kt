package by.aermakova.todoapp.data.useCase

import android.content.res.Resources
import androidx.lifecycle.LiveData
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.databinding.BottomSheetTaskActionBinding
import by.aermakova.todoapp.ui.goal.main.INIT_SELECTED_ITEM_ID
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.task.main.TasksViewModel
import by.aermakova.todoapp.data.useCase.actionEnum.TasksActionItem
import by.aermakova.todoapp.data.useCase.actionEnum.getLiveListOfActionsItems
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.disposables.CompositeDisposable


class TaskBottomSheetMenuUseCase(
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val taskActionBind: BottomSheetTaskActionBinding,
    private val dialog: BottomSheetDialog,
    private val taskActionItems: Array<TasksActionItem>,
    private val resources: Resources,
    private val mainFlowNavigation: MainFlowNavigation
) {

    private var selectedTaskId = INIT_SELECTED_ITEM_ID

    fun openBottomSheetActions(id: Long, viewModel: TasksViewModel) {
        taskActionBind.viewModel = viewModel
        selectedTaskId = id
        dialog.setContentView(taskActionBind.root)
        dialog.show()
    }

    fun getLiveListOfStepActionsItems(
        disposable: CompositeDisposable,
        errorAction: (String) -> Unit
    ): LiveData<List<CommonModel>> {
        return taskActionItems.getLiveListOfActionsItems(
            disposable,
            errorAction,
            resources,
            { item, disp, error -> stepAction(item, disp, error) }
        )
    }

    private fun stepAction(
        action: TasksActionItem,
        disposable: CompositeDisposable,
        errorAction: (String) -> Unit
    ) {
        dialog.dismiss()
        when (action) {
            TasksActionItem.EDIT_TASK -> mainFlowNavigation.navigateToEditElementFragment(
                selectedTaskId
            )
            TasksActionItem.DELETE_TASK -> deleteTaskUseCase.deleteById(
                selectedTaskId,
                disposable,
                errorAction
            )
        }
        selectedTaskId = INIT_SELECTED_ITEM_ID
    }
}
package by.aermakova.todoapp.data.useCase

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val mainFlowNavigation: MainFlowNavigation,
    private val findTaskUseCase: FindTaskUseCase
) {

    private var selectedTaskId = INIT_SELECTED_ITEM_ID

    private val _liveListOfTasksActionsItems = MutableLiveData<List<CommonModel>>()
    val liveListOfTasksActionsItems: LiveData<List<CommonModel>>
        get() = _liveListOfTasksActionsItems

    fun openBottomSheetActions(disposable: CompositeDisposable, id: Long, viewModel: TasksViewModel, errorAction: (String) -> Unit) {
        taskActionBind.viewModel = viewModel
        selectedTaskId = id
        checkIsTaskDone(disposable, selectedTaskId, errorAction)
    }

    private fun checkIsTaskDone(
        disposable: CompositeDisposable,
        taskId: Long,
        errorAction: (String) -> Unit
    ) {
        findTaskUseCase.useTasksById(
            disposable,
            taskId,
            {
                getLiveListOfStepActionsItems(disposable, it.taskStatusDone, errorAction)
                dialog.setContentView(taskActionBind.root)
                dialog.show()
            },
            errorAction
        )
    }

    private fun getLiveListOfStepActionsItems(
        disposable: CompositeDisposable,
        taskIsDone: Boolean,
        errorAction: (String) -> Unit
    ) {
        taskActionItems.getLiveListOfActionsItems(
            disposable,
            errorAction,
            resources,
            { item, disp, error -> stepAction(item, disp, error) }
        )
        _liveListOfTasksActionsItems.postValue(taskActionItems
            .filter { if (taskIsDone) it.forDone else true }
            .map { action ->
                action.toTextModel(resources) {
                    stepAction(action, disposable, errorAction)
                }
            }
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
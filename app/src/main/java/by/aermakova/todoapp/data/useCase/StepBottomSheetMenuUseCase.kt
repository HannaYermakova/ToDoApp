package by.aermakova.todoapp.data.useCase

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.useCase.actionEnum.StepsActionItem
import by.aermakova.todoapp.data.useCase.actionEnum.getLiveListOfActionsItems
import by.aermakova.todoapp.databinding.BottomSheetStepActionBinding
import by.aermakova.todoapp.ui.goal.main.INIT_SELECTED_ITEM_ID
import by.aermakova.todoapp.ui.idea.IdeasNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.step.main.StepsViewModel
import by.aermakova.todoapp.ui.task.TasksNavigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.disposables.CompositeDisposable


class StepBottomSheetMenuUseCase(
    private val addIdeaUseCase: AddItemToParentItemUseCase<IdeasNavigation>,
    private val addTaskUseCase: AddItemToParentItemUseCase<TasksNavigation>,
    val deleteStepUseCase: DeleteStepUseCase,
    private val stepActionBind: BottomSheetStepActionBinding,
    private val dialog: BottomSheetDialog,
    private val stepActionItems: Array<StepsActionItem>,
    private val resources: Resources,
    private val mainFlowNavigation: MainFlowNavigation,
    private val findStepUseCase: FindStepUseCase
) {

    private var selectedStepId = INIT_SELECTED_ITEM_ID

    private val _liveListOfStepActionsItems = MutableLiveData<List<CommonModel>>()
    val liveListOfStepActionsItems: LiveData<List<CommonModel>>
        get() = _liveListOfStepActionsItems

    fun openBottomSheetActions(
        disposable: CompositeDisposable,
        id: Long,
        viewModel: StepsViewModel,
        errorAction: (String) -> Unit
    ) {
        stepActionBind.viewModel = viewModel
        selectedStepId = id
        checkIsStepDone(disposable, selectedStepId, errorAction)
    }

    private fun checkIsStepDone(
        disposable: CompositeDisposable,
        stepId: Long,
        errorAction: (String) -> Unit
    ) {
        findStepUseCase.useStepById(
            disposable,
            stepId,
            {
                getLiveListOfStepActionsItems(disposable, it.stepStatusDone, errorAction)
                dialog.setContentView(stepActionBind.root)
                dialog.show()
            },
            errorAction
        )
    }

    private fun getLiveListOfStepActionsItems(
        disposable: CompositeDisposable,
        stepIsDone: Boolean,
        errorAction: (String) -> Unit
    ) {
        stepActionItems.getLiveListOfActionsItems(
            disposable,
            errorAction,
            resources,
            { item, disp, error -> stepAction(item, disp, error) }
        )
        _liveListOfStepActionsItems.postValue(stepActionItems
            .filter { if (stepIsDone) it.forDone else true }
            .map { action ->
                action.toTextModel(resources) {
                    stepAction(action, disposable, errorAction)
                }
            }
        )
    }


    private fun stepAction(
        action: StepsActionItem,
        disposable: CompositeDisposable,
        errorAction: (String) -> Unit
    ) {
        dialog.dismiss()
        when (action) {
            StepsActionItem.ADD_TASK_TO_STEP -> addTaskUseCase.checkGoalAndOpenDialog(
                disposable,
                selectedStepId,
                errorAction
            )
            StepsActionItem.ADD_IDEA_TO_STEP -> addIdeaUseCase.checkGoalAndOpenDialog(
                disposable,
                selectedStepId,
                errorAction
            )
            StepsActionItem.EDIT_STEP -> mainFlowNavigation.navigateToEditElementFragment(
                selectedStepId
            )
            StepsActionItem.DELETE_STEP -> deleteStepUseCase.confirmDeleteItem(
                selectedStepId,
                disposable,
                errorAction
            )
        }
        selectedStepId = INIT_SELECTED_ITEM_ID
    }
}
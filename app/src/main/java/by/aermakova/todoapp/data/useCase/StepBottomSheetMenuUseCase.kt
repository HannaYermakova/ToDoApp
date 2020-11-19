package by.aermakova.todoapp.data.useCase

import android.content.res.Resources
import androidx.lifecycle.LiveData
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.databinding.BottomSheetStepActionBinding
import by.aermakova.todoapp.ui.goal.main.INIT_SELECTED_ITEM_ID
import by.aermakova.todoapp.ui.idea.IdeasNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.step.main.StepsViewModel
import by.aermakova.todoapp.ui.task.TasksNavigation
import by.aermakova.todoapp.data.useCase.actionEnum.StepsActionItem
import by.aermakova.todoapp.data.useCase.actionEnum.getLiveListOfActionsItems
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
    private val mainFlowNavigation: MainFlowNavigation
) {

    private var selectedStepId = INIT_SELECTED_ITEM_ID

    fun openBottomSheetActions(id: Long, viewModel: StepsViewModel) {
        stepActionBind.viewModel = viewModel
        selectedStepId = id
        dialog.setContentView(stepActionBind.root)
        dialog.show()
    }

    fun getLiveListOfStepActionsItems(
        disposable: CompositeDisposable,
        errorAction: (String) -> Unit
    ): LiveData<List<CommonModel>> {
        return stepActionItems.getLiveListOfActionsItems(
            disposable,
            errorAction,
            resources,
            { item, disp, error -> stepAction(item, disp, error) }
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
            StepsActionItem.DELETE_STEP -> deleteStepUseCase.deleteById(
                selectedStepId,
                disposable,
                errorAction
            )
        }
        selectedStepId = INIT_SELECTED_ITEM_ID
    }
}
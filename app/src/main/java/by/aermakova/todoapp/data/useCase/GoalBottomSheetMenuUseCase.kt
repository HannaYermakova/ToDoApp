package by.aermakova.todoapp.data.useCase

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.useCase.actionEnum.GoalsActionItem
import by.aermakova.todoapp.data.useCase.actionEnum.getLiveListOfActionsItems
import by.aermakova.todoapp.databinding.BottomSheetGoalActionBinding
import by.aermakova.todoapp.ui.goal.main.GoalsViewModel
import by.aermakova.todoapp.ui.goal.main.INIT_SELECTED_ITEM_ID
import by.aermakova.todoapp.ui.idea.IdeasNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.step.StepsNavigation
import by.aermakova.todoapp.ui.task.TasksNavigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.disposables.CompositeDisposable


class GoalBottomSheetMenuUseCase(
    private val addTaskToGoalUseCase: AddItemToParentItemUseCase<TasksNavigation>,
    private val addStepToGoalUseCase: AddItemToParentItemUseCase<StepsNavigation>,
    private val addIdeaToGoalUseCase: AddItemToParentItemUseCase<IdeasNavigation>,
    private val deleteGoalUseCase: DeleteGoalUseCase,
    val addKeyResultToGoalUseCase: AddKeyResultToGoalUseCase,
    private val goalActionBind: BottomSheetGoalActionBinding,
    private val dialog: BottomSheetDialog,
    private val goalActionItems: Array<GoalsActionItem>,
    private val resources: Resources,
    private val mainFlowNavigation: MainFlowNavigation,
    private val findGoalUseCase: FindGoalUseCase
) {

    private var selectedGoalId = INIT_SELECTED_ITEM_ID

    val liveListOfGoalActionsItems = MutableLiveData<List<CommonModel>>()

    private fun getLiveListOfGoalActionsItems(
        disposable: CompositeDisposable,
        goalIsAchieved: Boolean,
        errorAction: (String) -> Unit,
    ) {
        goalActionItems.getLiveListOfActionsItems(
            disposable,
            errorAction,
            resources,
            itemAction = { item, disp, error -> goalAction(item, disp, error) }
        )
        val list = goalActionItems
            .filter { if (goalIsAchieved) it.forDone else true }
            .map { action ->
                action.toTextModel(resources) {
                    goalAction(action, disposable, errorAction)
                }
            }
        liveListOfGoalActionsItems.postValue(list)
    }

    fun openBottomSheetGoalsActions(
        disposable: CompositeDisposable,
        id: Long,
        viewModel: GoalsViewModel,
        errorAction: (String) -> Unit
    ) {
        goalActionBind.viewModel = viewModel
        selectedGoalId = id
        checkIsGoalAchieved(disposable, id, errorAction)
    }

    private fun checkIsGoalAchieved(
        disposable: CompositeDisposable,
        goalId: Long,
        errorAction: (String) -> Unit
    ) {
        findGoalUseCase.useGoalById(
            disposable,
            goalId,
            {
                getLiveListOfGoalActionsItems(disposable, it.goalStatusDone, errorAction)
                dialog.setContentView(goalActionBind.root)
                dialog.show()
            },
            errorAction
        )
    }

    fun addKeyResultToSelectedGoal(
        keyResultTitle: String,
        disposable: CompositeDisposable,
        errorAction: (String) -> Unit
    ) {
        addKeyResultToGoalUseCase.addKeyResult(
            selectedGoalId,
            keyResultTitle,
            disposable,
            errorAction = errorAction
        )
    }

    private fun goalAction(
        action: GoalsActionItem,
        disposable: CompositeDisposable,
        errorAction: (String) -> Unit
    ) {
        dialog.dismiss()
        when (action) {
            GoalsActionItem.ADD_KEY_RESULT_TO_GOAL -> addKeyResultToGoalUseCase.openDialog()
            GoalsActionItem.ADD_STEP_TO_GOAL -> addStepToGoalUseCase.checkGoalAndOpenDialog(
                disposable,
                selectedGoalId,
                errorAction
            )
            GoalsActionItem.ADD_TASK_TO_GOAL -> addTaskToGoalUseCase.checkGoalAndOpenDialog(
                disposable,
                selectedGoalId,
                errorAction
            )
            GoalsActionItem.ADD_IDEA_TO_GOAL -> addIdeaToGoalUseCase.checkGoalAndOpenDialog(
                disposable,
                selectedGoalId,
                errorAction
            )
            GoalsActionItem.EDIT_GOAL -> mainFlowNavigation.navigateToEditElementFragment(
                selectedGoalId
            )
            GoalsActionItem.DELETE_GOAL -> deleteGoalUseCase.deleteGoalById(
                selectedGoalId,
                disposable,
                errorAction
            )
        }
        selectedGoalId = INIT_SELECTED_ITEM_ID
    }
}
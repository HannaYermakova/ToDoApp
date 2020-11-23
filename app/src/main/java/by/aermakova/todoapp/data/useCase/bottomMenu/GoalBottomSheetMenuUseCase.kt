package by.aermakova.todoapp.data.useCase.bottomMenu

import android.content.res.Resources
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.db.entity.GoalEntity
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.useCase.AddItemToParentItemUseCase
import by.aermakova.todoapp.data.useCase.AddKeyResultToGoalUseCase
import by.aermakova.todoapp.data.useCase.DeleteGoalUseCase
import by.aermakova.todoapp.data.useCase.FindGoalUseCase
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
    private val mainFlowNavigation: MainFlowNavigation,
    private val findGoalUseCase: FindGoalUseCase,
    dialog: BottomSheetDialog,
    goalActionItems: Array<GoalsActionItem>,
    resources: Resources
) : CommonBottomSheetMenuUseCase<GoalsViewModel, GoalEntity, DeleteGoalUseCase, GoalsActionItem>(
    deleteGoalUseCase, dialog, goalActionItems, resources
) {
    override fun bindViewModel(viewModel: GoalsViewModel) {
        goalActionBind.viewModel = viewModel
    }

    override val rootView: View
        get() = goalActionBind.root

    override fun useItemById(
        disposable: CompositeDisposable,
        itemId: Long,
        function: (GoalEntity) -> Unit,
        errorAction: (String) -> Unit
    ) {
        findGoalUseCase.useGoalById(
            disposable,
            itemId,
            function,
            errorAction
        )
    }

    override fun checkIsItemDone(entity: GoalEntity) = entity.goalStatusDone

    override fun setItemAction(
        action: GoalsActionItem,
        disposable: CompositeDisposable,
        errorAction: (String) -> Unit
    ) {
        when (action) {
            GoalsActionItem.ADD_KEY_RESULT_TO_GOAL -> addKeyResultToGoalUseCase.openDialog()
            GoalsActionItem.ADD_STEP_TO_GOAL -> addStepToGoalUseCase.checkGoalAndOpenDialog(
                disposable,
                selectedItemId,
                errorAction
            )
            GoalsActionItem.ADD_TASK_TO_GOAL -> addTaskToGoalUseCase.checkGoalAndOpenDialog(
                disposable,
                selectedItemId,
                errorAction
            )
            GoalsActionItem.ADD_IDEA_TO_GOAL -> addIdeaToGoalUseCase.checkGoalAndOpenDialog(
                disposable,
                selectedItemId,
                errorAction
            )
            GoalsActionItem.EDIT_GOAL -> mainFlowNavigation.navigateToEditElementFragment(
                selectedItemId
            )
            GoalsActionItem.DELETE_GOAL -> deleteGoalUseCase.confirmDeleteItem(
                selectedItemId,
                disposable,
                errorAction
            )
        }
    }

    fun addKeyResultToSelectedGoal(
        keyResultTitle: String,
        disposable: CompositeDisposable,
        errorAction: (String) -> Unit
    ) {
        addKeyResultToGoalUseCase.addKeyResult(
            selectedItemId,
            keyResultTitle,
            disposable,
            errorAction = errorAction
        )
    }


}
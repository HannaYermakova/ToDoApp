package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.databinding.BottomSheetGoalActionBinding
import by.aermakova.todoapp.ui.goal.main.GoalsViewModel
import by.aermakova.todoapp.ui.goal.main.INIT_SELECTED_GOAL_ID
import by.aermakova.todoapp.ui.idea.IdeasNavigation
import by.aermakova.todoapp.ui.step.StepsNavigation
import by.aermakova.todoapp.ui.task.TasksNavigation
import by.aermakova.todoapp.util.GoalsActionItem
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Named

class GoalBottomSheetMenuUseCase(
    @Named("AddTaskUseCase") private val addTaskToGoalUseCase: AddItemToGoalUseCase<TasksNavigation>,
    @Named("AddStepUseCase") private val addStepToGoalUseCase: AddItemToGoalUseCase<StepsNavigation>,
    @Named("AddIdeaUseCase") private val addIdeaToGoalUseCase: AddItemToGoalUseCase<IdeasNavigation>,
    val addKeyResultToGoalUseCase: AddKeyResultToGoalUseCase,
    private val goalActionBind: BottomSheetGoalActionBinding,
    private val dialog: BottomSheetDialog
) {

    private var selectedGoalId = INIT_SELECTED_GOAL_ID

    fun openBottomSheetGoalsActions(id: Long, viewModel: GoalsViewModel) {
        goalActionBind.viewModel = viewModel
        selectedGoalId = id
        dialog.setContentView(goalActionBind.root)
        dialog.show()
    }

    fun addKeyResultToSelectedGoal(
        keyResultTitle: String,
        disposable: CompositeDisposable,
        errorAction: (String) -> Unit
    ) {
        addKeyResultToGoalUseCase.addKeyResult(
            keyResultTitle,
            disposable,
            errorAction
        )
    }

    fun goalAction(
        action: GoalsActionItem,
        disposable: CompositeDisposable,
        errorAction: (String) -> Unit
    ) {
        dialog.dismiss()
        when (action) {
            GoalsActionItem.ADD_KEY_RESULT_TO_GOAL -> addKeyResultToGoalUseCase.openDialog(
                selectedGoalId
            )
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
        }
        selectedGoalId = INIT_SELECTED_GOAL_ID
    }

}
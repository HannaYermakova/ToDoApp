package by.aermakova.todoapp.data.useCase

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.model.toTextModel
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
    private val deleteGoalUseCase: DeleteGoalUseCase,
    val addKeyResultToGoalUseCase: AddKeyResultToGoalUseCase,
    private val goalActionBind: BottomSheetGoalActionBinding,
    private val dialog: BottomSheetDialog,
    private val goalActionItems: Array<GoalsActionItem>,
    private val resources: Resources
) {

    private var selectedGoalId = INIT_SELECTED_GOAL_ID

    fun getLiveListOfGoalActionsItems(
        disposable: CompositeDisposable,
        errorAction: (String) -> Unit
    ): LiveData<List<CommonModel>> {
        val liveList = MutableLiveData<List<CommonModel>>()
        val list = goalActionItems
            .map { action ->
                action.toTextModel(resources) {
                    goalAction(action, disposable, errorAction)
                }
            }
        liveList.postValue(list)
        return liveList
    }

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
            selectedGoalId,
            keyResultTitle,
            disposable,
            errorAction
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
            GoalsActionItem.EDIT_GOAL -> Log.d("A_GoalBottomSheetMen", "Edit Goal")
            GoalsActionItem.DELETE_GOAL -> deleteGoalUseCase.deleteGoalById(
                selectedGoalId,
                disposable,
                errorAction
            )
        }
        selectedGoalId = INIT_SELECTED_GOAL_ID
    }

}
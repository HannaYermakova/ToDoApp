package by.aermakova.todoapp.data.useCase.bottomMenu

import android.content.res.Resources
import android.view.View
import by.aermakova.todoapp.data.db.entity.StepEntity
import by.aermakova.todoapp.data.useCase.AddItemToParentItemUseCase
import by.aermakova.todoapp.data.useCase.DeleteStepUseCase
import by.aermakova.todoapp.data.useCase.FindStepUseCase
import by.aermakova.todoapp.data.useCase.actionEnum.StepsActionItem
import by.aermakova.todoapp.databinding.BottomSheetStepActionBinding
import by.aermakova.todoapp.ui.idea.IdeasNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.step.main.StepsViewModel
import by.aermakova.todoapp.ui.task.TasksNavigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.disposables.CompositeDisposable


class StepBottomSheetMenuUseCase(
    val deleteStepUseCase: DeleteStepUseCase,
    private val addIdeaUseCase: AddItemToParentItemUseCase<IdeasNavigation>,
    private val addTaskUseCase: AddItemToParentItemUseCase<TasksNavigation>,
    private val stepActionBind: BottomSheetStepActionBinding,
    private val findStepUseCase: FindStepUseCase,
    private val mainFlowNavigation: MainFlowNavigation,
    dialog: BottomSheetDialog,
    stepActionItems: Array<StepsActionItem>,
    resources: Resources
) : CommonBottomSheetMenuUseCase<StepsViewModel, StepEntity, DeleteStepUseCase, StepsActionItem>(
    deleteStepUseCase, dialog, stepActionItems, resources
) {
    override fun bindViewModel(viewModel: StepsViewModel) {
        stepActionBind.viewModel = viewModel
    }

    override val rootView: View
        get() = stepActionBind.root

    override fun useItemById(
        disposable: CompositeDisposable,
        itemId: Long,
        function: (StepEntity) -> Unit,
        errorAction: (String) -> Unit
    ) {
        findStepUseCase.useStepById(
            disposable,
            itemId,
            function,
            errorAction
        )
    }

    override fun checkIsItemDone(entity: StepEntity) = entity.stepStatusDone

    override fun setItemAction(
        action: StepsActionItem,
        disposable: CompositeDisposable,
        errorAction: (String) -> Unit
    ) {
        when (action) {
            StepsActionItem.ADD_TASK_TO_STEP -> addTaskUseCase.checkGoalAndOpenDialog(
                disposable,
                selectedItemId,
                errorAction
            )
            StepsActionItem.ADD_IDEA_TO_STEP -> addIdeaUseCase.checkGoalAndOpenDialog(
                disposable,
                selectedItemId,
                errorAction
            )
            StepsActionItem.EDIT_STEP -> mainFlowNavigation.navigateToEditElementFragment(
                selectedItemId
            )
            StepsActionItem.DELETE_STEP -> deleteStepUseCase.confirmDeleteItem(
                selectedItemId,
                disposable,
                errorAction
            )
        }
    }
}
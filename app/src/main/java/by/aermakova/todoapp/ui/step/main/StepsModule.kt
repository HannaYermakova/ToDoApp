package by.aermakova.todoapp.ui.step.main

import android.app.Activity
import android.content.res.Resources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.di.scope.*
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.data.useCase.*
import by.aermakova.todoapp.data.useCase.actionEnum.StepsActionItem
import by.aermakova.todoapp.databinding.BottomSheetStepActionBinding
import by.aermakova.todoapp.ui.dialog.confirm.ConfirmDialogNavigation
import by.aermakova.todoapp.ui.idea.IdeasNavigation
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.ui.step.StepsNavigation
import by.aermakova.todoapp.ui.task.TasksNavigation
import by.aermakova.todoapp.util.Item
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap


@Module
class StepsModule {

    @Provides
    @AddIdeaUseCase
    fun provideAddIdeaToGoalUseCase(
        stepInteractor: StepInteractor,
        @NavigationIdeas ideasNavigation: IdeasNavigation,
        @ErrorStepIsDoneIdea errorStepIsDoneIdea: String
    ): AddItemToParentItemUseCase<IdeasNavigation> {
        return AddItemToParentItemUseCase(
            stepInteractor,
            Item.STEP,
            ideasNavigation,
            errorStepIsDoneIdea
        )
    }

    @Provides
    @AddTaskUseCase
    fun provideAddTaskToGoalUseCase(
        stepInteractor: StepInteractor,
        @NavigationTasks tasksNavigation: TasksNavigation,
        @ErrorStepIsDoneTask errorStepIsDoneTask: String
    ): AddItemToParentItemUseCase<TasksNavigation> {
        return AddItemToParentItemUseCase(
            stepInteractor,
            Item.STEP,
            tasksNavigation,
            errorStepIsDoneTask
        )
    }

    @Provides
    fun provideGoalBottomSheetMenuUseCase(
        @AddIdeaUseCase addIdeaToStepUseCase: AddItemToParentItemUseCase<IdeasNavigation>,
        @AddTaskUseCase addTaskToStepUseCase: AddItemToParentItemUseCase<TasksNavigation>,
        deleteStepUseCase: DeleteStepUseCase,
        stepActionBind: BottomSheetStepActionBinding,
        dialog: BottomSheetDialog,
        stepActionItems: Array<StepsActionItem>,
        resources: Resources,
        @NavigationSteps mainFlowNavigation: StepsNavigation,
        findStepUseCase: FindStepUseCase
    ) =
        StepBottomSheetMenuUseCase(
            addIdeaToStepUseCase,
            addTaskToStepUseCase,
            deleteStepUseCase,
            stepActionBind,
            dialog,
            stepActionItems,
            resources,
            mainFlowNavigation,
            findStepUseCase
        )

    @Provides
    fun provideFindStepUseCase(
        stepInteractor: StepInteractor,
        @ErrorWhileLoading errorMessage: String
    ) =
        FindStepUseCase(
            stepInteractor, errorMessage
        )

    @Provides
    fun provideResources(activity: Activity): Resources =
        activity.resources

    @Provides
    fun provideBottomSheetDialog(activity: Activity): BottomSheetDialog =
        BottomSheetDialog(activity)

    @Provides
    fun provideStepsActionItem(): Array<StepsActionItem> {
        return StepsActionItem.values()
    }

    @Provides
    fun provideBottomSheetStepActionBinding(fragment: StepsFragment): BottomSheetStepActionBinding {
        val bind: BottomSheetStepActionBinding = DataBindingUtil.inflate(
            fragment.layoutInflater,
            R.layout.bottom_sheet_step_action,
            null,
            false
        )
        bind.lifecycleOwner = fragment
        return bind
    }

    @Provides
    fun provideDeleteStepUseCase(
        @DialogConfirm dialogNavigation: DialogNavigation<Boolean>,
        @TitleDialogDeleteStep dialogTitle: String,
        goalInteractor: GoalInteractor,
        stepInteractor: StepInteractor,
        taskInteractor: TaskInteractor,
        ideaInteractor: IdeaInteractor,
        @ErrorDeleteStep errorMessage: String
    ) = DeleteStepUseCase(
        goalInteractor,
        stepInteractor,
        taskInteractor,
        ideaInteractor,
        errorMessage,
        dialogNavigation,
        dialogTitle
    )

    @Provides
    @DialogConfirm
    fun provideDialogNavigation(controller: NavController): DialogNavigation<Boolean> =
        ConfirmDialogNavigation(controller)

    @Provides
    @ErrorDeleteStep
    fun provideErrorDeleteStepMessage(activity: Activity) =
        activity.getString(R.string.error_delete_step)

    @Provides
    @TitleDialogDeleteStep
    fun provideDeleteStepDialogTitle(activity: Activity) =
        activity.getString(R.string.confirm_delete_step)

    @Provides
    fun provideLoadAllStepsUseCase(
        stepInteractor: StepInteractor,
        @ErrorWhileLoading errorMessage: String
    ) =
        LoadAllStepsUseCase(stepInteractor, errorMessage)

    @Provides
    @ErrorWhileLoading
    fun provideErrorMessage(activity: Activity) =
        activity.getString(R.string.error_while_loading)

    @Provides
    @ErrorStepIsDoneIdea
    fun provideStepIsDoneIdeaErrorMessage(activity: Activity) =
        activity.getString(R.string.error_adding_idea_to_step)

    @Provides
    @ErrorStepIsDoneTask
    fun provideStepIsDoneTaskErrorMessage(activity: Activity) =
        activity.getString(R.string.error_adding_task_to_step)

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    @NavigationSteps
    fun provideStepsNavigation(controller: NavController) =
        StepsNavigation(controller)

    @Provides
    @NavigationIdeas
    fun provideIdeasNavigation(controller: NavController) =
        IdeasNavigation(controller)

    @Provides
    @NavigationTasks
    fun provideTasksNavigation(controller: NavController) =
        TasksNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(StepsViewModel::class)
    fun provideViewModel(viewModel: StepsViewModel): ViewModel = viewModel
}
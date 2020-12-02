package by.aermakova.todoapp.ui.goal.main

import android.app.Activity
import android.content.res.Resources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.di.scope.*
import by.aermakova.todoapp.data.interactor.*
import by.aermakova.todoapp.data.useCase.AddItemToParentItemUseCase
import by.aermakova.todoapp.data.useCase.AddKeyResultToGoalUseCase
import by.aermakova.todoapp.data.useCase.FindGoalUseCase
import by.aermakova.todoapp.data.useCase.actionEnum.GoalsActionItem
import by.aermakova.todoapp.data.useCase.bottomMenu.GoalBottomSheetMenuUseCase
import by.aermakova.todoapp.data.useCase.delete.DeleteGoalUseCase
import by.aermakova.todoapp.databinding.BottomSheetGoalActionBinding
import by.aermakova.todoapp.ui.dialog.addItem.AddItemDialogNavigation
import by.aermakova.todoapp.ui.dialog.confirm.ConfirmDialogNavigation
import by.aermakova.todoapp.ui.goal.GoalsNavigation
import by.aermakova.todoapp.ui.idea.IdeasNavigation
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.step.StepsNavigation
import by.aermakova.todoapp.ui.task.TasksNavigation
import by.aermakova.todoapp.util.Item
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap


@Module
class GoalsModule {

    @Provides
    fun provideGoalBottomSheetMenuUseCase(
        @AddTaskUseCase addTaskToGoalUseCase: AddItemToParentItemUseCase<TasksNavigation>,
        @AddStepUseCase addStepToGoalUseCase: AddItemToParentItemUseCase<StepsNavigation>,
        @AddIdeaUseCase addIdeaToGoalUseCase: AddItemToParentItemUseCase<IdeasNavigation>,
        @NavigationGoals mainFlowNavigation: MainFlowNavigation,
        deleteGoalUseCase: DeleteGoalUseCase,
        addKeyResultToGoalUseCase: AddKeyResultToGoalUseCase,
        goalActionBind: BottomSheetGoalActionBinding,
        dialog: BottomSheetDialog,
        goalActionItems: Array<GoalsActionItem>,
        resources: Resources,
        findGoalUseCase: FindGoalUseCase
    ) =
        GoalBottomSheetMenuUseCase(
            addTaskToGoalUseCase,
            addStepToGoalUseCase,
            addIdeaToGoalUseCase,
            deleteGoalUseCase,
            addKeyResultToGoalUseCase,
            goalActionBind,
            mainFlowNavigation,
            findGoalUseCase,
            dialog,
            goalActionItems,
            resources
        )

    @Provides
    fun provideFindGoalUseCase(
        goalInteractor: GoalInteractor,
        @ErrorFindGoal errorMessage: String
    ) =
        FindGoalUseCase(goalInteractor, errorMessage)

    @Provides
    @ErrorFindGoal
    fun provideFindGoalErrorMessage(activity: Activity) =
        activity.getString(R.string.error_find_goal)

    @Provides
    fun provideDeleteGoalUseCase(
        goalInteractor: GoalInteractor,
        keyResultInteractor: KeyResultInteractor,
        stepInteractor: StepInteractor,
        taskInteractor: TaskInteractor,
        ideaInteractor: IdeaInteractor,
        @ErrorDeleteGoal errorDeleteGoalMessage: String,
        controller: NavController,
        @TitleDialogDeleteGoal dialogTitle: String,
    ) = DeleteGoalUseCase(
        goalInteractor,
        keyResultInteractor,
        stepInteractor,
        taskInteractor,
        ideaInteractor,
        errorDeleteGoalMessage,
        ConfirmDialogNavigation(controller, dialogTitle),
        dialogTitle
    )

    @Provides
    @TitleDialogDeleteGoal
    fun provideTitleDialogDeleteGoal(activity: Activity) =
        activity.getString(R.string.confirm_delete_goal)

    @Provides
    @TitleDialogLogout
    fun provideTitleDialogLogout(activity: Activity) =
        activity.getString(R.string.confirm_exit_message)

    @Provides
    @AddIdeaUseCase
    fun provideAddIdeaToGoalUseCase(
        goalInteractor: GoalInteractor,
        @NavigationIdeas ideasNavigation: IdeasNavigation,
        @ErrorGoalIsDoneIdea errorAchievedGoalIdea: String
    ): AddItemToParentItemUseCase<IdeasNavigation> {
        return AddItemToParentItemUseCase(
            goalInteractor,
            Item.GOAL,
            ideasNavigation,
            errorAchievedGoalIdea
        )
    }

    @Provides
    @AddStepUseCase
    fun provideAddStepToGoalUseCase(
        goalInteractor: GoalInteractor,
        @NavigationSteps stepsNavigation: StepsNavigation,
        @ErrorGoalIsDoneStep errorAchievedGoalStep: String
    ): AddItemToParentItemUseCase<StepsNavigation> {
        return AddItemToParentItemUseCase(
            goalInteractor,
            Item.GOAL,
            stepsNavigation,
            errorAchievedGoalStep
        )
    }

    @Provides
    @AddTaskUseCase
    fun provideAddTaskToGoalUseCase(
        goalInteractor: GoalInteractor,
        @NavigationTasks tasksNavigation: TasksNavigation,
        @ErrorGoalIsDoneTask errorAchievedGoalTask: String
    ): AddItemToParentItemUseCase<TasksNavigation> {
        return AddItemToParentItemUseCase(
            goalInteractor,
            Item.GOAL,
            tasksNavigation,
            errorAchievedGoalTask
        )
    }

    @Provides
    @NavigationTasks
    fun provideTasksNavigation(controller: NavController): TasksNavigation =
        TasksNavigation(controller)

    @Provides
    @NavigationSteps
    fun provideStepsNavigation(controller: NavController): StepsNavigation =
        StepsNavigation(controller)

    @Provides
    @NavigationIdeas
    fun provideIdeasNavigation(controller: NavController): IdeasNavigation =
        IdeasNavigation(controller)

    @Provides
    fun provideFilterBottomSheetBinding(fragment: GoalsFragment): BottomSheetGoalActionBinding {
        val bind: BottomSheetGoalActionBinding = DataBindingUtil.inflate(
            fragment.layoutInflater,
            R.layout.bottom_sheet_goal_action,
            null,
            false
        )
        bind.lifecycleOwner = fragment
        return bind
    }

    @Provides
    fun provideAddKeyResultToGoalUseCase(
        goalInteractor: GoalInteractor,
        @DialogAddItem dialogNavigation: AddItemDialogNavigation,
        activity: Activity
    ) =
        AddKeyResultToGoalUseCase(
            goalInteractor,
            dialogNavigation,
            activity.resources.getString(R.string.add_key_result),
            activity.resources.getString(R.string.error_adding_key_result)
        )

    @Provides
    @ErrorGoalIsDoneTask
    fun provideErrorAchievedGoalTask(activity: Activity) =
        activity.getString(R.string.error_adding_task)

    @Provides
    @ErrorGoalIsDoneStep
    fun provideErrorAchievedGoalStep(activity: Activity) =
        activity.getString(R.string.error_adding_step)

    @Provides
    @ErrorGoalIsDoneIdea
    fun provideErrorAchievedGoalIdea(activity: Activity) =
        activity.getString(R.string.error_adding_idea)

    @Provides
    @ErrorDeleteGoal
    fun provideErrorDeleteGoal(activity: Activity) =
        activity.getString(R.string.error_delete_goal)

    @Provides
    fun provideBottomSheetDialog(activity: Activity): BottomSheetDialog =
        BottomSheetDialog(activity)

    @Provides
    fun provideResources(activity: Activity): Resources =
        activity.resources

    @Provides
    fun provideGoalActionItems(): Array<GoalsActionItem> {
        return GoalsActionItem.values()
    }

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    @DialogAddItem
    fun provideAddItemDialogNavigation(controller: NavController): AddItemDialogNavigation =
        AddItemDialogNavigation(controller)

    @Provides
    @DialogConfirm
    fun provideDialogNavigation(
        controller: NavController,
        @TitleDialogLogout tag: String
    ): DialogNavigation<Boolean> =
        ConfirmDialogNavigation(controller, tag)

    @Provides
    @NavigationGoals
    fun provideGoalsNavigation(controller: NavController): MainFlowNavigation =
        GoalsNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(GoalsViewModel::class)
    fun provideViewModel(goalsViewModel: GoalsViewModel): ViewModel = goalsViewModel
}
package by.aermakova.todoapp.ui.step.main

import android.app.Activity
import android.content.res.Resources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.data.useCase.AddItemToParentItemUseCase
import by.aermakova.todoapp.data.useCase.DeleteStepUseCase
import by.aermakova.todoapp.data.useCase.LoadAllStepsUseCase
import by.aermakova.todoapp.data.useCase.StepBottomSheetMenuUseCase
import by.aermakova.todoapp.databinding.BottomSheetStepActionBinding
import by.aermakova.todoapp.ui.idea.IdeasNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.step.StepsNavigation
import by.aermakova.todoapp.ui.task.TasksNavigation
import by.aermakova.todoapp.util.Item
import by.aermakova.todoapp.util.StepsActionItem
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
class StepsModule {

    @Provides
    @Named("AddIdeaUseCase")
    fun provideAddIdeaToGoalUseCase(
        stepInteractor: StepInteractor,
        @Named("IdeasNavigation") ideasNavigation: IdeasNavigation,
        @Named("StepIsDoneIdea") errorStepIsDoneIdea: String
    ): AddItemToParentItemUseCase<IdeasNavigation> {
        return AddItemToParentItemUseCase(
            stepInteractor,
            Item.STEP,
            ideasNavigation,
            errorStepIsDoneIdea
        )
    }

    @Provides
    @Named("AddTaskUseCase")
    fun provideAddTaskToGoalUseCase(
        stepInteractor: StepInteractor,
        @Named("TasksNavigation") tasksNavigation: TasksNavigation,
        @Named("StepIsDoneTask") errorStepIsDoneTask: String
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
        @Named("AddIdeaUseCase") addIdeaToStepUseCase: AddItemToParentItemUseCase<IdeasNavigation>,
        @Named("AddTaskUseCase") addTaskToStepUseCase: AddItemToParentItemUseCase<TasksNavigation>,
        deleteStepUseCase: DeleteStepUseCase,
        stepActionBind: BottomSheetStepActionBinding,
        dialog: BottomSheetDialog,
        stepActionItems: Array<StepsActionItem>,
        resources: Resources,
        mainFlowNavigation: MainFlowNavigation
    ) =
        StepBottomSheetMenuUseCase(
            addIdeaToStepUseCase,
            addTaskToStepUseCase,
            deleteStepUseCase,
            stepActionBind,
            dialog,
            stepActionItems,
            resources,
            mainFlowNavigation
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
        goalInteractor: GoalInteractor,
        stepInteractor: StepInteractor,
        taskInteractor: TaskInteractor,
        ideaInteractor: IdeaInteractor,
        @Named("DeleteStep") errorMessage: String
    ) = DeleteStepUseCase(
        goalInteractor,
        stepInteractor,
        taskInteractor,
        ideaInteractor,
        errorMessage
    )

    @Provides
    @Named("DeleteStep")
    fun provideErrorDeleteStepMessage(activity: Activity) =
        activity.getString(R.string.error_delete_step)

    @Provides
    fun provideLoadAllStepsUseCase(
        stepInteractor: StepInteractor,
        @Named("LoadSteps") errorMessage: String
    ) =
        LoadAllStepsUseCase(stepInteractor, errorMessage)

    @Provides
    @Named("LoadSteps")
    fun provideErrorMessage(activity: Activity) =
        activity.getString(R.string.error_while_loading)

    @Provides
    @Named("StepIsDoneIdea")
    fun provideStepIsDoneIdeaErrorMessage(activity: Activity) =
        activity.getString(R.string.error_adding_idea_to_step)

    @Provides
    @Named("StepIsDoneTask")
    fun provideStepIsDoneTaskErrorMessage(activity: Activity) =
        activity.getString(R.string.error_adding_task_to_step)

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    fun provideStepsNavigation(controller: NavController): MainFlowNavigation =
        StepsNavigation(controller)

    @Provides
    @Named("IdeasNavigation")
    fun provideIdeasNavigation(controller: NavController): IdeasNavigation =
        IdeasNavigation(controller)

    @Provides
    @Named("TasksNavigation")
    fun provideTasksNavigation(controller: NavController): TasksNavigation =
        TasksNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(StepsViewModel::class)
    fun provideViewModel(viewModel: StepsViewModel): ViewModel = viewModel
}
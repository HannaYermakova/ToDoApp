package by.aermakova.todoapp.ui.task.edit

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.di.scope.*
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.data.useCase.EditTaskUseCase
import by.aermakova.todoapp.data.useCase.FindGoalUseCase
import by.aermakova.todoapp.data.useCase.FindStepUseCase
import by.aermakova.todoapp.data.useCase.SetTaskFieldsUseCase
import by.aermakova.todoapp.ui.dialog.datePicker.PickDayDialogNavigator
import by.aermakova.todoapp.ui.task.TasksNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class EditTaskModule {

    @Provides
    fun provideTaskId(fragment: EditTaskFragment): Long {
        return fragment.navArgs<EditTaskFragmentArgs>().value.taskId
    }

    @Provides
    fun provideEditTaskUseCase(
        taskInteractor: TaskInteractor,
        pickDayDialogNavigation: PickDayDialogNavigator,
        @ErrorEditTask errorMessage: String
    ) = EditTaskUseCase(taskInteractor, pickDayDialogNavigation, errorMessage)

    @Provides
    fun provideSetTaskFieldsUseCase(
        taskInteractor: TaskInteractor,
        findGoalUseCase: FindGoalUseCase,
        findStepUseCase: FindStepUseCase,
        @ErrorSetTaskFields errorMessage: String
    ) =
        SetTaskFieldsUseCase(taskInteractor, findGoalUseCase, findStepUseCase, errorMessage)

    @Provides
    fun provideFindStepUseCase(
        stepInteractor: StepInteractor,
        @ErrorFindStep errorMessage: String
    ) =
        FindStepUseCase(
            stepInteractor, errorMessage
        )

    @Provides
    fun provideDialogNavigation(controller: NavController): PickDayDialogNavigator {
        return PickDayDialogNavigator(controller)
    }

    @Provides
    fun provideFindGoalUseCase(
        goalInteractor: GoalInteractor,
        @ErrorFindGoal errorMessage: String
    ) =
        FindGoalUseCase(goalInteractor, errorMessage)

    @Provides
    @ErrorEditTask
    fun provideEditTaskErrorMessage(activity: Activity) =
        activity.getString(R.string.error_while_updating_task)

    @Provides
    @ErrorSetTaskFields
    fun provideSetTaskFieldsErrorMessage(activity: Activity) =
        activity.getString(R.string.error_while_setting_task_fields)

    @Provides
    @ErrorFindGoal
    fun provideFindGoalErrorMessage(activity: Activity) =
        activity.getString(R.string.error_find_goal)

    @Provides
    @ErrorFindStep
    fun provideFindStepErrorMessage(activity: Activity) =
        activity.getString(R.string.error_find_step)

    @Provides
    @NavigationSteps
    fun provideTaskNavigation(controller: NavController) =
        TasksNavigation(controller)

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    @IntoMap
    @ViewModelKey(EditTaskViewModel::class)
    fun provideViewModel(viewModel: EditTaskViewModel): ViewModel = viewModel
}
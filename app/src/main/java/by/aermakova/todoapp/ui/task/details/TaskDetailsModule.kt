package by.aermakova.todoapp.ui.task.details

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.di.scope.ErrorFindGoal
import by.aermakova.todoapp.data.di.scope.ErrorFindStep
import by.aermakova.todoapp.data.di.scope.NavigationTasks
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.useCase.FindGoalUseCase
import by.aermakova.todoapp.data.useCase.FindStepUseCase
import by.aermakova.todoapp.ui.task.TasksNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap


@Module
class TaskDetailsModule {

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
    @ErrorFindStep
    fun provideFindStepErrorMessage(activity: Activity) =
        activity.getString(R.string.error_find_step)

    @Provides
    fun provideFindStepUseCase(
        stepInteractor: StepInteractor,
        @ErrorFindStep errorMessage: String
    ) =
        FindStepUseCase(stepInteractor, errorMessage)

    @Provides
    fun provideArgs(fragment: TaskDetailsFragment): Long {
        return fragment.navArgs<TaskDetailsFragmentArgs>().value.taskId
    }

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    @NavigationTasks
    fun provideTasksNavigation(controller: NavController) =
        TasksNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(TaskDetailsViewModel::class)
    fun provideViewModel(viewModel: TaskDetailsViewModel): ViewModel = viewModel
}
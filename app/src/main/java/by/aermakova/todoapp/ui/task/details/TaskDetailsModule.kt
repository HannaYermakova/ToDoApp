package by.aermakova.todoapp.ui.task.details

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.useCase.FindGoalUseCase
import by.aermakova.todoapp.data.useCase.FindStepUseCase
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.task.TasksNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
class TaskDetailsModule {

    @Provides
    fun provideFindGoalUseCase(
        goalInteractor: GoalInteractor,
        @Named("FindGoal") errorMessage: String
    ) =
        FindGoalUseCase(goalInteractor, errorMessage)

    @Provides
    @Named("FindGoal")
    fun provideFindGoalErrorMessage(activity: Activity) =
        activity.getString(R.string.error_find_goal)

    @Provides
    @Named("FindStep")
    fun provideFindStepErrorMessage(activity: Activity) =
        activity.getString(R.string.error_find_step)

    @Provides
    fun provideFindStepUseCase(
        stepInteractor: StepInteractor,
        @Named("FindStep") errorMessage: String
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
    fun provideTasksNavigation(controller: NavController): MainFlowNavigation =
        TasksNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(TaskDetailsViewModel::class)
    fun provideViewModel(viewModel: TaskDetailsViewModel): ViewModel = viewModel
}
package by.aermakova.todoapp.ui.task.addNew

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.di.scope.NavigationTasks
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.useCase.FindStepUseCase
import by.aermakova.todoapp.data.useCase.GoalSelectUseCase
import by.aermakova.todoapp.data.useCase.KeyResultSelectUseCase
import by.aermakova.todoapp.data.useCase.StepSelectUseCase
import by.aermakova.todoapp.ui.dialog.datePicker.PickDayDialogNavigator
import by.aermakova.todoapp.ui.task.TasksNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class AddTaskModule {

    @Provides
    fun provideFindStepUseCase(
        stepInteractor: StepInteractor,
        errorMessage: String
    ) =
        FindStepUseCase(stepInteractor, errorMessage)

    @Provides
    fun provideArgs(fragment: AddTaskFragment): Long {
        return fragment.navArgs<AddTaskFragmentArgs>().value.id
    }

    @Provides
    fun provideArgsItem(fragment: AddTaskFragment): Int {
        return fragment.navArgs<AddTaskFragmentArgs>().value.code
    }

    @Provides
    fun provideGoalSelectUseCase(goalInteractor: GoalInteractor) =
        GoalSelectUseCase(goalInteractor)

    @Provides
    fun provideKeyResultSelectUseCase(goalInteractor: GoalInteractor) =
        KeyResultSelectUseCase(goalInteractor)

    @Provides
    fun provideStepSelectUseCase(stepInteractor: StepInteractor) =
        StepSelectUseCase(stepInteractor)

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    @NavigationTasks
    fun provideTasksNavigation(controller: NavController) =
        TasksNavigation(controller)

    @Provides
    fun provideDialogNavigation(controller: NavController): PickDayDialogNavigator {
        return PickDayDialogNavigator(controller)
    }

    @Provides
    fun provideErrorMessage(activity: Activity): String =
        activity.getString(R.string.error_empty_field_task)

    @Provides
    @IntoMap
    @ViewModelKey(AddTaskViewModel::class)
    fun provideViewModel(viewModel: AddTaskViewModel): ViewModel = viewModel
}
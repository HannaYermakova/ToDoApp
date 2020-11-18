package by.aermakova.todoapp.ui.step.addNew

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.di.scope.NavigationSteps
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.useCase.CreateStepUseCase
import by.aermakova.todoapp.data.useCase.GoalSelectUseCase
import by.aermakova.todoapp.data.useCase.KeyResultSelectUseCase
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.step.StepsNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class AddStepModule {

    @Provides
    fun provideArgs(fragment: AddStepFragment): Long {
        return fragment.navArgs<AddStepFragmentArgs>().value.id
    }

    @Provides
    fun provideArgsItem(fragment: AddStepFragment): Int {
        return fragment.navArgs<AddStepFragmentArgs>().value.code
    }

    @Provides
    fun provideCreateStepUseCase(
        stepInteractor: StepInteractor,
        errorMessage: String
    ) =
        CreateStepUseCase(stepInteractor, errorMessage)

    @Provides
    fun provideGoalSelectUseCase(goalInteractor: GoalInteractor) =
        GoalSelectUseCase(goalInteractor)

    @Provides
    fun provideKeyResultSelectUseCase(goalInteractor: GoalInteractor) =
        KeyResultSelectUseCase(goalInteractor)

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    @NavigationSteps
    fun provideTasksNavigation(controller: NavController): MainFlowNavigation =
        StepsNavigation(controller)

    @Provides
    fun provideErrorMessage(activity: Activity): String {
        return activity.getString(R.string.error_empty_field_step)
    }

    @Provides
    @IntoMap
    @ViewModelKey(AddStepViewModel::class)
    fun provideViewModel(viewModel: AddStepViewModel): ViewModel = viewModel
}
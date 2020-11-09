package by.aermakova.todoapp.ui.step.main

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.useCase.LoadAllStepsUseCase
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.step.StepsNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class StepsModule {

    @Provides
    fun provideLoadAllStepsUseCase(
        stepInteractor: StepInteractor,
        errorMessage: String
    ) =
        LoadAllStepsUseCase(stepInteractor, errorMessage)

    @Provides
    fun provideErrorMessage(activity: Activity) =
        activity.getString(R.string.error_while_loading)

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    fun provideTasksNavigation(controller: NavController): MainFlowNavigation =
        StepsNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(StepsViewModel::class)
    fun provideViewModel(viewModel: StepsViewModel): ViewModel = viewModel
}
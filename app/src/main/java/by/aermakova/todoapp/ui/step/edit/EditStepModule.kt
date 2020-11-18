package by.aermakova.todoapp.ui.step.edit

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.di.scope.ErrorWhileLoading
import by.aermakova.todoapp.data.di.scope.NavigationSteps
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.useCase.ChangeStepTextUseCase
import by.aermakova.todoapp.data.useCase.FindStepUseCase
import by.aermakova.todoapp.ui.step.StepsNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap


@Module
class EditStepModule {

    @Provides
    fun provideChangeStepTextUseCase(
        stepInteractor: StepInteractor,
        @ErrorWhileLoading errorMessage: String
    ) =
        ChangeStepTextUseCase(
            stepInteractor, errorMessage
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
    @ErrorWhileLoading
    fun provideErrorMessage(activity: Activity): String =
        activity.getString(R.string.error_while_loading)

    @Provides
    fun provideStepId(fragment: EditStepFragment): Long {
        return fragment.navArgs<EditStepFragmentArgs>().value.stepId
    }

    @Provides
    @NavigationSteps
    fun provideStepNavigation(controller: NavController) =
        StepsNavigation(controller)

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    @IntoMap
    @ViewModelKey(EditStepViewModel::class)
    fun provideViewModel(viewModel: EditStepViewModel): ViewModel = viewModel
}
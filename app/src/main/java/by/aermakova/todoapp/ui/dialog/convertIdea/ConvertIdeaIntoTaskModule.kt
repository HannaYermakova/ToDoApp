package by.aermakova.todoapp.ui.dialog.convertIdea

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.di.scope.DialogPickDate
import by.aermakova.todoapp.data.di.scope.NavigationConvertIdea
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.data.useCase.CreateTaskUseCase
import by.aermakova.todoapp.data.useCase.KeyResultSelectUseCase
import by.aermakova.todoapp.ui.dialog.datePicker.PickDayDialogNavigator
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap


@Module
class ConvertIdeaIntoTaskModule {

    @Provides
    fun provideCreateTaskUseCase(
        @DialogPickDate pickDayDialogNavigation: PickDayDialogNavigator,
        taskInteractor: TaskInteractor,
        errorMessage: String
    ) = CreateTaskUseCase(
        pickDayDialogNavigation,
        taskInteractor,
        errorMessage
    )

    @Provides
    fun provideArgs(fragment: ConvertIdeaIntoTaskDialogFragment): Long {
        return fragment.navArgs<ConvertIdeaIntoTaskDialogFragmentArgs>().value.ideaId
    }

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    fun provideKeyResultSelectUseCase(goalInteractor: GoalInteractor) =
        KeyResultSelectUseCase(goalInteractor)

    @Provides
    @NavigationConvertIdea
    fun provideConvertDialogNavigation(controller: NavController) =
        ConvertIdeaDialogNavigator(controller)

    @Provides
    @DialogPickDate
    fun provideDialogNavigation(controller: NavController): PickDayDialogNavigator =
        PickDayDialogNavigator(controller)

    @Provides
    fun provideErrorMessage(activity: Activity): String =
        activity.resources.getString(R.string.error_empty_field_task)

    @Provides
    @IntoMap
    @ViewModelKey(ConvertIdeaIntoTaskViewModel::class)
    fun provideViewModel(viewModel: ConvertIdeaIntoTaskViewModel): ViewModel = viewModel
}
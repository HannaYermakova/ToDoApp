package by.aermakova.todoapp.ui.goal.addNew

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.di.scope.NavigationGoals
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.useCase.CreateGoalUseCase
import by.aermakova.todoapp.ui.dialog.addItem.AddItemDialogNavigation
import by.aermakova.todoapp.ui.goal.GoalsNavigation
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class AddGoalModule {

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    fun provideCreateGoalUseCase(
        goalInteractor: GoalInteractor,
        errorMessage: String
    ) = CreateGoalUseCase(
        goalInteractor, errorMessage
    )

    @Provides
    fun provideErrorMessage(activity: Activity): String =
        activity.getString(R.string.error_empty_field_goal)

    @Provides
    @NavigationGoals
    fun provideGoalsNavigation(controller: NavController): MainFlowNavigation =
        GoalsNavigation(controller)

    @Provides
    fun provideDialogNavigation(controller: NavController): DialogNavigation<String> =
        AddItemDialogNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(AddGoalViewModel::class)
    fun provideViewModel(viewModel: AddGoalViewModel): ViewModel = viewModel
}
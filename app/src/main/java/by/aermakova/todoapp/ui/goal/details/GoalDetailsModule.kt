package by.aermakova.todoapp.ui.goal.details

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.di.scope.NavigationGoals
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.useCase.FindGoalUseCase
import by.aermakova.todoapp.data.useCase.LoadAllGoalsUseCase
import by.aermakova.todoapp.ui.goal.GoalsNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class GoalDetailsModule {

    @Provides
    fun provideLoadAllGoalsUseCase(
        goalInteractor: GoalInteractor,
        errorMessage: String
    ) =
        LoadAllGoalsUseCase(goalInteractor, errorMessage)

    @Provides
    fun provideErrorMessage(activity: Activity) =
        activity.getString(R.string.error_while_loading)

    @Provides
    fun provideFindGoalUseCase(
        goalInteractor: GoalInteractor,
        errorMessage: String
    ) =
        FindGoalUseCase(goalInteractor, errorMessage)

    @Provides
    fun provideArgs(fragment: GoalDetailsFragment): Long {
        return fragment.navArgs<GoalDetailsFragmentArgs>().value.goalId
    }

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    @NavigationGoals
    fun provideGoalsNavigation(controller: NavController): MainFlowNavigation =
        GoalsNavigation(controller)


    @Provides
    @IntoMap
    @ViewModelKey(GoalDetailsViewModel::class)
    fun provideGoalDetailsViewModel(viewModel: GoalDetailsViewModel): ViewModel = viewModel
}
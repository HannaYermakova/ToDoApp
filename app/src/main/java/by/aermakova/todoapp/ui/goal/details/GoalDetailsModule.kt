package by.aermakova.todoapp.ui.goal.details

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.ui.dialog.AddItemDialogNavigation
import by.aermakova.todoapp.ui.goal.GoalsNavigation
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class GoalDetailsModule {

    @Provides
    fun provideArgs(fragment: GoalDetailsFragment): Long {
        return fragment.navArgs<GoalDetailsFragmentArgs>().value.goalId
    }

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    fun provideGoalsNavigation(controller: NavController): MainFlowNavigation =
        GoalsNavigation(controller)

    @Provides
    fun provideDialogNavigation(controller: NavController): DialogNavigation =
        AddItemDialogNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(GoalDetailsViewModel::class)
    fun provideGoalDetailsViewModel(viewModel: GoalDetailsViewModel): ViewModel = viewModel
}
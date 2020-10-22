package by.aermakova.todoapp.ui.step.addNew

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.ui.dialog.selectItem.goal.SelectGoalDialogNavigation
import by.aermakova.todoapp.ui.dialog.selectItem.keyResult.SelectKeyResultDialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.step.StepsNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
class AddStepModule {

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    fun provideTasksNavigation(controller: NavController): MainFlowNavigation =
        StepsNavigation(controller)

    @Provides
    @Named("SelectGoal")
    fun provideSelectGoalDialogNavigation(controller: NavController): SelectGoalDialogNavigation =
        SelectGoalDialogNavigation(controller)

    @Provides
    @Named("SelectKeyResult")
    fun provideSelectKeyResDialogNavigation(controller: NavController): SelectKeyResultDialogNavigation =
        SelectKeyResultDialogNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(AddStepViewModel::class)
    fun provideViewModel(viewModel: AddStepViewModel): ViewModel = viewModel
}
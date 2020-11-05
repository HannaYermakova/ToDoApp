package by.aermakova.todoapp.ui.idea.addNew

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.ui.dialog.selectItem.goal.SelectGoalDialogNavigation
import by.aermakova.todoapp.ui.dialog.selectItem.keyResult.SelectKeyResultDialogNavigation
import by.aermakova.todoapp.ui.dialog.selectItem.step.SelectStepDialogNavigation
import by.aermakova.todoapp.ui.idea.IdeasNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
class AddIdeaModule {

    @Provides
    @Named("SelectGoal")
    fun provideSelectGoalDialogNavigation(controller: NavController): SelectGoalDialogNavigation =
        SelectGoalDialogNavigation(controller)

    @Provides
    @Named("SelectKeyResult")
    fun provideSelectKeyResDialogNavigation(controller: NavController): SelectKeyResultDialogNavigation =
        SelectKeyResultDialogNavigation(controller)

    @Provides
    @Named("SelectStep")
    fun provideSelectStepDialogNavigation(controller: NavController): SelectStepDialogNavigation =
        SelectStepDialogNavigation(controller)

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    fun provideTasksNavigation(controller: NavController): MainFlowNavigation =
        IdeasNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(AddIdeaViewModel::class)
    fun provideViewModel(viewModel: AddIdeaViewModel): ViewModel = viewModel
}
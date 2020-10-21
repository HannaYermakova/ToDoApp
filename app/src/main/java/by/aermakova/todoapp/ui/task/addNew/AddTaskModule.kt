package by.aermakova.todoapp.ui.task.addNew

import android.app.Activity
import android.app.DatePickerDialog
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.ui.dialog.datePicker.PickDayDialogNavigator
import by.aermakova.todoapp.ui.dialog.selectItem.goal.SelectGoalDialogNavigation
import by.aermakova.todoapp.ui.dialog.selectItem.keyResult.SelectKeyResultDialogNavigation
import by.aermakova.todoapp.ui.dialog.selectItem.step.SelectStepDialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.task.TasksNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
class AddTaskModule {

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    fun provideTasksNavigation(controller: NavController): MainFlowNavigation =
        TasksNavigation(controller)

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
    @Named("PickDate")
    fun provideDialogNavigation(controller: NavController): PickDayDialogNavigator {
        return PickDayDialogNavigator(controller)
    }

    @Provides
    @IntoMap
    @ViewModelKey(AddTaskViewModel::class)
    fun provideViewModel(viewModel: AddTaskViewModel): ViewModel = viewModel
}
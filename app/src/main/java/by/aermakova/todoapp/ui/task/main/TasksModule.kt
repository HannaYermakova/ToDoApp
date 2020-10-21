package by.aermakova.todoapp.ui.task.main

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.task.TasksNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class TasksModule {

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    fun provideTasksNavigation(controller: NavController): MainFlowNavigation =
        TasksNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(TasksViewModel::class)
    fun provideViewModel(viewModel: TasksViewModel): ViewModel = viewModel
}
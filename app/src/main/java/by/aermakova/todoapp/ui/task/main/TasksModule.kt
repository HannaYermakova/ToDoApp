package by.aermakova.todoapp.ui.task.main

import android.app.Activity
import android.content.res.Resources
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.databinding.BottomSheetFilterTaskBinding
import by.aermakova.todoapp.databinding.BottomSheetSortTaskBinding
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.task.TasksNavigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class TasksModule {

    @Provides
    fun provideFilterBottomSheetBinding(fragment: TasksFragment): BottomSheetFilterTaskBinding {
        val bind: BottomSheetFilterTaskBinding = DataBindingUtil.inflate(
            fragment.layoutInflater,
            R.layout.bottom_sheet_filter_task,
            null,
            false
        )
        bind.lifecycleOwner = fragment
        return bind
    }

    @Provides
    fun provideSortBottomSheetBinding(fragment: TasksFragment): BottomSheetSortTaskBinding {
        val bind: BottomSheetSortTaskBinding = DataBindingUtil.inflate(
            fragment.layoutInflater,
            R.layout.bottom_sheet_sort_task,
            null,
            false
        )
        bind.lifecycleOwner = fragment
        return bind
    }

    @Provides
    fun provideBottomSheetDialog(activity: Activity): BottomSheetDialog =
        BottomSheetDialog(activity)

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    fun provideResources(activity: Activity): Resources =
        activity.resources

    @Provides
    fun provideTasksNavigation(controller: NavController): MainFlowNavigation =
        TasksNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(TasksViewModel::class)
    fun provideViewModel(viewModel: TasksViewModel): TasksViewModel = viewModel
}
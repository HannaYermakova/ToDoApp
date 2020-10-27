package by.aermakova.todoapp.ui.task.main

import android.app.Activity
import android.content.res.Resources
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.databinding.FilterBottomSheetBinding
import by.aermakova.todoapp.databinding.SortBottomSheetBinding
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.task.TasksNavigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class TasksModule {

    @Provides
    fun provideFilterBottomSheetBinding(fragment: TasksFragment): FilterBottomSheetBinding {
        val bind: FilterBottomSheetBinding = DataBindingUtil.inflate(
            fragment.layoutInflater,
            R.layout.filter_bottom_sheet,
            null,
            false
        )
        bind.lifecycleOwner = fragment
        return bind
    }

    @Provides
    fun provideSortBottomSheetBinding(fragment: TasksFragment): SortBottomSheetBinding {
        val bind: SortBottomSheetBinding = DataBindingUtil.inflate(
            fragment.layoutInflater,
            R.layout.sort_bottom_sheet,
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
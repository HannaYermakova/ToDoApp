package by.aermakova.todoapp.ui.dialog.selectItem

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.ui.dialog.selectItem.goal.SelectGoalDialogNavigation
import by.aermakova.todoapp.ui.dialog.selectItem.goal.SelectGoalViewModel
import by.aermakova.todoapp.ui.dialog.selectItem.keyResult.SelectKeyResultDialogNavigation
import by.aermakova.todoapp.ui.dialog.selectItem.keyResult.SelectKeyResultViewModel
import by.aermakova.todoapp.ui.dialog.selectItem.step.SelectStepDialogNavigation
import by.aermakova.todoapp.ui.dialog.selectItem.step.SelectStepViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class SelectItemModule {

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    fun provideSelectGoalDialogNavigation(controller: NavController): SelectGoalDialogNavigation =
        SelectGoalDialogNavigation(controller)

    @Provides
    fun provideSelectKeyResultDialogNavigation(controller: NavController): SelectKeyResultDialogNavigation =
        SelectKeyResultDialogNavigation(controller)

    @Provides
    fun provideSelectStepDialogNavigation(controller: NavController): SelectStepDialogNavigation =
        SelectStepDialogNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(SelectGoalViewModel::class)
    fun provideGoalViewModel(viewModel: SelectGoalViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelKey(SelectKeyResultViewModel::class)
    fun provideKeyResultViewModel(viewModel: SelectKeyResultViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelKey(SelectStepViewModel::class)
    fun provideStepViewModel(viewModel: SelectStepViewModel): ViewModel = viewModel
}
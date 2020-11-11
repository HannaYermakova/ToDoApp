package by.aermakova.todoapp.ui.goal.main

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.ui.dialog.addItem.AddItemDialogNavigation
import by.aermakova.todoapp.ui.dialog.confirm.ConfirmDialogNavigation
import by.aermakova.todoapp.ui.goal.GoalsNavigation
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named


@Module
class GoalsModule {

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    @Named("AddItemDialog")
    fun provideAddItemDialogNavigation(controller: NavController): AddItemDialogNavigation =
        AddItemDialogNavigation(controller)

    @Provides
    @Named("ConfirmDialog")
    fun provideDialogNavigation(controller: NavController): DialogNavigation<Boolean> =
        ConfirmDialogNavigation(controller)

    @Provides
    fun provideGoalsNavigation(controller: NavController): MainFlowNavigation {
        return GoalsNavigation(controller)
    }

    @Provides
    @IntoMap
    @ViewModelKey(GoalsViewModel::class)
    fun provideViewModel(goalsViewModel: GoalsViewModel): ViewModel = goalsViewModel
}
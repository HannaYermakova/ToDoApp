package by.aermakova.todoapp.ui.goal.main

import android.app.Activity
import android.content.res.Resources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.useCase.AddKeyResultToGoalUseCase
import by.aermakova.todoapp.databinding.BottomSheetGoalActionBinding
import by.aermakova.todoapp.ui.dialog.addItem.AddItemDialogNavigation
import by.aermakova.todoapp.ui.dialog.confirm.ConfirmDialogNavigation
import by.aermakova.todoapp.ui.goal.GoalsNavigation
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.GoalsActionItem
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named


@Module
class GoalsModule {

    @Provides
    fun provideFilterBottomSheetBinding(fragment: GoalsFragment): BottomSheetGoalActionBinding {
        val bind: BottomSheetGoalActionBinding = DataBindingUtil.inflate(
            fragment.layoutInflater,
            R.layout.bottom_sheet_goal_action,
            null,
            false
        )
        bind.lifecycleOwner = fragment
        return bind
    }

    @Provides
    fun provideAddKeyResultToGoalUseCase(
        goalInteractor: GoalInteractor,
        @Named("AddItemDialog") dialogNavigation: AddItemDialogNavigation,
        activity: Activity
    ) =
        AddKeyResultToGoalUseCase(
            goalInteractor,
            dialogNavigation,
            activity.resources.getString(R.string.add_key_result),
            activity.resources.getString(R.string.error_adding_key_result)
        )

    @Provides
    fun provideBottomSheetDialog(activity: Activity): BottomSheetDialog =
        BottomSheetDialog(activity)

    @Provides
    fun provideResources(activity: Activity): Resources =
        activity.resources

    @Provides
    fun provideGoalActionItems(): Array<GoalsActionItem> {
        return GoalsActionItem.values()
    }

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
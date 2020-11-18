package by.aermakova.todoapp.ui.goal.edit

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.di.scope.*
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.useCase.AddKeyResultToGoalUseCase
import by.aermakova.todoapp.data.useCase.AddNewKeyResultsToGoalUseCase
import by.aermakova.todoapp.data.useCase.ChangeGoalTextUseCase
import by.aermakova.todoapp.data.useCase.FindGoalUseCase
import by.aermakova.todoapp.ui.dialog.addItem.AddItemDialogNavigation
import by.aermakova.todoapp.ui.goal.GoalsNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap


@Module
class EditGoalModule {

    @Provides
    fun provideAddNewKeyResultsToGoalUseCase(
        @DialogAddItem dialogNavigation: AddItemDialogNavigation,
        @TitleAddKeyResult addKeyResultDialogTitle: String,
        @ErrorAddKeyResult errorMessage: String,
        addKeyResultToGoalUseCase: AddKeyResultToGoalUseCase
    ) =
        AddNewKeyResultsToGoalUseCase(
            dialogNavigation,
            addKeyResultDialogTitle,
            errorMessage,
            addKeyResultToGoalUseCase
        )

    @Provides
    fun provideAddKeyResultToGoalUseCase(
        goalInteractor: GoalInteractor,
        @DialogAddItem dialogNavigation: AddItemDialogNavigation,
        @TitleAddKeyResult addKeyResultDialogTitle: String,
        @ErrorAddKeyResult errorMessage: String,
    ) =
        AddKeyResultToGoalUseCase(
            goalInteractor,
            dialogNavigation,
            addKeyResultDialogTitle,
            errorMessage
        )

    @Provides
    fun provideFindGoalUseCase(
        goalInteractor: GoalInteractor,
        @ErrorFindGoal errorMessage: String
    ) =
        FindGoalUseCase(goalInteractor, errorMessage)

    @Provides
    fun provideChangeGoalTextUseCase(
        goalInteractor: GoalInteractor,
        @ErrorChangeTitleGoal errorMessage: String
    ) = ChangeGoalTextUseCase(
        goalInteractor, errorMessage
    )

    @Provides
    fun provideGoalId(fragment: EditGoalFragment): Long {
        return fragment.navArgs<EditGoalFragmentArgs>().value.goalId
    }

    @Provides
    @DialogAddItem
    fun provideAddItemDialogNavigation(controller: NavController): AddItemDialogNavigation =
        AddItemDialogNavigation(controller)

    @Provides
    @ErrorAddKeyResult
    fun provideAddKeyResultErrorMessage(activity: Activity) =
        activity.getString(R.string.error_adding_key_result)

    @Provides
    @TitleAddKeyResult
    fun provideAddKeyResultTitle(activity: Activity) =
        activity.getString(R.string.add_key_result)

    @Provides
    @ErrorFindGoal
    fun provideFindGoalErrorMessage(activity: Activity) =
        activity.getString(R.string.error_find_goal)

    @Provides
    @ErrorChangeTitleGoal
    fun provideErrorMessageChangeText(activity: Activity) =
        activity.getString(R.string.error_change_goal_text)

    @Provides
    @NavigationGoals
    fun provideGoalNavigation(controller: NavController): MainFlowNavigation =
        GoalsNavigation(controller)

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    @IntoMap
    @ViewModelKey(EditGoalViewModel::class)
    fun provideViewModel(viewModel: EditGoalViewModel): ViewModel = viewModel
}
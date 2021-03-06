package by.aermakova.todoapp.ui.idea.details

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.di.scope.*
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.useCase.CreateStepUseCase
import by.aermakova.todoapp.data.useCase.FindGoalUseCase
import by.aermakova.todoapp.data.useCase.FindStepUseCase
import by.aermakova.todoapp.data.useCase.LoadIdeaDetailsUseCase
import by.aermakova.todoapp.ui.dialog.convertIdea.ConvertIdeaDialogNavigator
import by.aermakova.todoapp.ui.dialog.selectItem.keyResult.SelectKeyResultDialogNavigation
import by.aermakova.todoapp.ui.idea.IdeasNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
class IdeaDetailsModule {

    @Provides
    fun provideLoadIdeaDetailsUseCase(
        ideaInteractor: IdeaInteractor,
        findGoal: FindGoalUseCase,
        findStep: FindStepUseCase,
        @ErrorWhileLoading errorMessage: String
    ) =
        LoadIdeaDetailsUseCase(
            ideaInteractor,
            findGoal,
            findStep,
            errorMessage
        )

    @Provides
    @ErrorWhileLoading
    fun provideErrorMessage(activity: Activity): String =
        activity.getString(R.string.error_while_loading)

    @Provides
    fun provideCreateStepUseCase(
        stepInteractor: StepInteractor,
        @ErrorWhileLoading errorMessage: String
    ) =
        CreateStepUseCase(stepInteractor, errorMessage)

    @Provides
    fun provideFindGoalUseCase(
        goalInteractor: GoalInteractor,
        @ErrorWhileLoading errorMessage: String
    ) =
        FindGoalUseCase(goalInteractor, errorMessage)

    @Provides
    fun provideFindStepUseCase(
        stepInteractor: StepInteractor,
        @ErrorWhileLoading errorMessage: String
    ) =
        FindStepUseCase(stepInteractor, errorMessage)

    @Provides
    fun provideArgs(fragment: IdeaDetailsFragment): Long {
        return fragment.navArgs<IdeaDetailsFragmentArgs>().value.ideaId
    }

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    @NavigationIdeas
    fun provideTasksNavigation(controller: NavController): MainFlowNavigation =
        IdeasNavigation(controller)

    @Provides
    @DialogConvertIdea
    fun provideConvertDialogNavigation(controller: NavController) =
        ConvertIdeaDialogNavigator(controller)

    @Provides
    @DialogSelectKeyResult
    fun provideSelectKeyResDialogNavigation(controller: NavController) =
        SelectKeyResultDialogNavigation(controller)

    @Provides
    @TitleSelectKeyResult
    fun provideSelectKeyResultTitle(activity: Activity): String =
        activity.getString(R.string.select_key_result_text)

    @Provides
    @IntoMap
    @ViewModelKey(IdeaDetailsViewModel::class)
    fun provideViewModel(viewModel: IdeaDetailsViewModel): ViewModel = viewModel
}
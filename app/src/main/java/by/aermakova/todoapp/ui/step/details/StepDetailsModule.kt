package by.aermakova.todoapp.ui.step.details

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.di.scope.NavigationSteps
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.data.useCase.FindGoalUseCase
import by.aermakova.todoapp.data.useCase.FindIdeaUseCase
import by.aermakova.todoapp.data.useCase.FindTaskUseCase
import by.aermakova.todoapp.data.useCase.LoadStepUseCase
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.step.StepsNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
class StepDetailsModule {

    @Provides
    fun provideLoadStepUserCase(
        stepInteractor: StepInteractor,
        tasksInteractor: TaskInteractor,
        findGoal: FindGoalUseCase,
        findTask: FindTaskUseCase,
        findIdea: FindIdeaUseCase,
        stepId: Long,
        @Named("LoadingError") errorMessage: String
    ) =
        LoadStepUseCase(
            stepInteractor,
            tasksInteractor,
            findGoal,
            findTask,
            findIdea,
            stepId,
            errorMessage
        )

    @Provides
    @Named("LoadingError")
    fun provideLoadErrorMessage(activity: Activity) =
        activity.getString(R.string.error_while_loading)

    @Provides
    fun provideFindIdeaUseCase(
        ideaInteractor: IdeaInteractor
    ) =
        FindIdeaUseCase(ideaInteractor)

    @Provides
    fun provideFindTaskUseCase(taskInteractor: TaskInteractor) =
        FindTaskUseCase(taskInteractor)

    @Provides
    fun provideFindGoalUseCase(
        goalInteractor: GoalInteractor,
        @Named("LoadingError") errorMessage: String
    ) =
        FindGoalUseCase(goalInteractor, errorMessage)

    @Provides
    fun provideArgs(fragment: StepDetailsFragment): Long {
        return fragment.navArgs<StepDetailsFragmentArgs>().value.stepId
    }

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    @NavigationSteps
    fun provideTasksNavigation(controller: NavController): MainFlowNavigation =
        StepsNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(StepDetailsViewModel::class)
    fun provideViewModel(viewModel: StepDetailsViewModel): ViewModel = viewModel
}
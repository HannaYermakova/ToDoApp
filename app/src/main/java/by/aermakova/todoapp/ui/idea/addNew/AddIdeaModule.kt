package by.aermakova.todoapp.ui.idea.addNew

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.di.scope.NavigationIdeas
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.useCase.*
import by.aermakova.todoapp.ui.idea.IdeasNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class AddIdeaModule {

    @Provides
    fun provideArgsId(fragment: AddIdeaFragment): Long {
        return fragment.navArgs<AddIdeaFragmentArgs>().value.id
    }

    @Provides
    fun provideArgsItem(fragment: AddIdeaFragment): Int {
        return fragment.navArgs<AddIdeaFragmentArgs>().value.code
    }

    @Provides
    fun provideCreateStepUseCase(
        stepInteractor: StepInteractor,
        errorMessage: String
    ) =
        CreateStepUseCase(stepInteractor, errorMessage)

    @Provides
    fun provideCreateIdeaUseCase(
        ideaInteractor: IdeaInteractor,
        errorMessage: String
    ) =
        CreateIdeaUseCase(ideaInteractor, errorMessage)

    @Provides
    fun provideFindStepUseCase(
        stepInteractor: StepInteractor,
        errorMessage: String
    ) =
        FindStepUseCase(stepInteractor, errorMessage)

    @Provides
    fun provideGoalSelectUseCase(goalInteractor: GoalInteractor) =
        GoalSelectUseCase(goalInteractor)

    @Provides
    fun provideKeyResultSelectUseCase(goalInteractor: GoalInteractor) =
        KeyResultSelectUseCase(goalInteractor)

    @Provides
    fun provideStepSelectUseCase(stepInteractor: StepInteractor) =
        StepSelectUseCase(stepInteractor)

    @Provides
    fun provideErrorMessage(activity: Activity): String =
        activity.getString(R.string.error_empty_field_idea)

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    @NavigationIdeas
    fun provideTasksNavigation(controller: NavController): MainFlowNavigation =
        IdeasNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(AddIdeaViewModel::class)
    fun provideViewModel(viewModel: AddIdeaViewModel): ViewModel = viewModel
}
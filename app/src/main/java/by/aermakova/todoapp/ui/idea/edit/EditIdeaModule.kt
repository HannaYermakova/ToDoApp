package by.aermakova.todoapp.ui.idea.edit

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.di.scope.ErrorEditIdea
import by.aermakova.todoapp.data.di.scope.NavigationIdeas
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.useCase.FindIdeaUseCase
import by.aermakova.todoapp.data.useCase.editText.ChangeItemTextUseCase
import by.aermakova.todoapp.ui.idea.IdeasNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class EditIdeaModule {

    @Provides
    fun provideChangeIdeaTextUseCase(
        ideaInteractor: IdeaInteractor,
        @ErrorEditIdea errorMessage: String
    ) = ChangeItemTextUseCase(
        ideaInteractor, errorMessage
    )

    @Provides
    fun provideFindIdeaUseCase(
        ideaInteractor: IdeaInteractor
    ) = FindIdeaUseCase(ideaInteractor)

    @Provides
    @ErrorEditIdea
    fun provideErrorEditIdea(activity: Activity) =
        activity.getString(R.string.error_editing_idea)


    @Provides
    fun provideIdeaId(fragment: EditIdeaFragment): Long {
        return fragment.navArgs<EditIdeaFragmentArgs>().value.ideaId
    }

    @Provides
    @NavigationIdeas
    fun provideIdeaNavigation(controller: NavController) =
        IdeasNavigation(controller)

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    @IntoMap
    @ViewModelKey(EditIdeaViewModel::class)
    fun provideEditIdeaViewModel(viewModel: EditIdeaViewModel): ViewModel = viewModel
}
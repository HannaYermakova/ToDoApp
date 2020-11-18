package by.aermakova.todoapp.ui.idea.main

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.di.scope.ErrorWhileLoading
import by.aermakova.todoapp.data.di.scope.NavigationIdeas
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.useCase.LoadAllIdeasUseCase
import by.aermakova.todoapp.ui.idea.IdeasNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class IdeaModule {

    @Provides
    fun provideLoadAllIdeasUseCase(
        ideaInteractor: IdeaInteractor,
        @ErrorWhileLoading errorMessage: String
    ) =
        LoadAllIdeasUseCase(ideaInteractor, errorMessage)

    @Provides
    @ErrorWhileLoading
    fun provideErrorMessage(activity: Activity) =
        activity.getString(R.string.error_while_loading)

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    @NavigationIdeas
    fun provideIdeasNavigation(controller: NavController): MainFlowNavigation =
        IdeasNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(IdeaViewModel::class)
    fun provideViewModel(viewModel: IdeaViewModel): ViewModel = viewModel
}
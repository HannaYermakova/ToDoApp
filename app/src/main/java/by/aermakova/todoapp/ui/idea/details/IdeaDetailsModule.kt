package by.aermakova.todoapp.ui.idea.details

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.ui.idea.IdeasNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class IdeaDetailsModule {

    @Provides
    fun provideArgs(fragment: IdeaDetailsFragment): Long {
        return fragment.navArgs<IdeaDetailsFragmentArgs>().value.ideaId
    }

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    fun provideTasksNavigation(controller: NavController): MainFlowNavigation =
        IdeasNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(IdeaDetailsViewModel::class)
    fun provideViewModel(viewModel: IdeaDetailsViewModel): ViewModel = viewModel
}
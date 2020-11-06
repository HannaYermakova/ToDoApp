package by.aermakova.todoapp.ui.idea.addNew

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.ui.idea.IdeasNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class AddIdeaModule {

    @Provides
    fun provideErrorMessage(activity: Activity): String =
        activity.getString(R.string.error_empty_field_idea)

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    fun provideTasksNavigation(controller: NavController): MainFlowNavigation =
        IdeasNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(AddIdeaViewModel::class)
    fun provideViewModel(viewModel: AddIdeaViewModel): ViewModel = viewModel
}
package by.aermakova.todoapp.ui.dialog.convertIdea

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.ui.dialog.datePicker.PickDayDialogNavigator
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
class ConvertIdeaIntoTaskModule {

    @Provides
    fun provideArgs(fragment: ConvertIdeaIntoTaskDialogFragment): Long {
        return fragment.navArgs<ConvertIdeaIntoTaskDialogFragmentArgs>().value.ideaId
    }

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    @Named("ConvertIdea")
    fun provideConvertDialogNavigation(controller: NavController): ConvertIdeaDialogNavigator {
        return ConvertIdeaDialogNavigator(controller)
    }

    @Provides
    @Named("PickDate")
    fun provideDialogNavigation(controller: NavController): PickDayDialogNavigator {
        return PickDayDialogNavigator(controller)
    }

    @Provides
    @IntoMap
    @ViewModelKey(ConvertIdeaIntoTaskViewModel::class)
    fun provideViewModel(viewModel: ConvertIdeaIntoTaskViewModel): ViewModel = viewModel
}
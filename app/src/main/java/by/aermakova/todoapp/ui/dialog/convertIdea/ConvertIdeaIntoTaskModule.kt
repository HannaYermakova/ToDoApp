package by.aermakova.todoapp.ui.dialog.convertIdea

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.navArgs
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
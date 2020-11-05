package by.aermakova.todoapp.ui.dialog.confirm

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class ConfirmModule {

    @Provides
    fun provideDialogNavigation(activity: Activity): DialogNavigation<Boolean> {
        val navController = Navigation.findNavController(activity, R.id.app_host_fragment)
        return ConfirmDialogNavigation(navController)
    }

    @Provides
    fun provideArgs(fragment: ConfirmDialogFragment): String {
        return fragment.navArgs<ConfirmDialogFragmentArgs>().value.title
    }

    @Provides
    @IntoMap
    @ViewModelKey(ConfirmDialogViewModel::class)
    fun provideViewModel(viewModel: ConfirmDialogViewModel): ViewModel = viewModel
}
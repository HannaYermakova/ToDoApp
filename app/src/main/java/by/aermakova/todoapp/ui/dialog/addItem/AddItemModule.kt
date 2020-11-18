package by.aermakova.todoapp.ui.dialog.addItem

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.di.scope.ErrorEmptyField
import by.aermakova.todoapp.data.di.scope.TitleDialogAddItem
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap


@Module
class AddItemModule {

    @Provides
    fun provideDialogNavigation(activity: Activity): DialogNavigation<String> {
        val navController = Navigation.findNavController(activity, R.id.app_host_fragment)
        return AddItemDialogNavigation(navController)
    }

    @Provides
    @TitleDialogAddItem
    fun provideArgs(fragment: AddItemDialogFragment): String {
        return fragment.navArgs<AddItemDialogFragmentArgs>().value.title
    }

    @Provides
    @ErrorEmptyField
    fun provideErrorMessage(activity: Activity)=
         activity.getString(R.string.error_empty_field)

    @Provides
    @IntoMap
    @ViewModelKey(AddItemDialogViewModel::class)
    fun provideViewModel(viewModel: AddItemDialogViewModel): ViewModel = viewModel
}
package by.aermakova.todoapp.ui.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.ui.main.MainFlowNavigationSetting
import by.aermakova.todoapp.ui.navigation.AuthFlowNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class AuthFlowModule {

    @Provides
    fun provideAuthFlowNavigation(activity: Activity): AuthFlowNavigation.Settings {
        val controller = Navigation.findNavController(activity, R.id.auth_host_fragment)
        return AuthFlowNavigationSettings(controller)
    }

    @Provides
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    fun provideViewModel(viewModel: AuthViewModel): ViewModel = viewModel
}
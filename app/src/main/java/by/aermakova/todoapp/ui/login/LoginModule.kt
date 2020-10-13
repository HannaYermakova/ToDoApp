package by.aermakova.todoapp.ui.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class LoginModule {

    @Provides
    fun provideFacebookLoginHelper(loginNavigation: LoginNavigation) =
       FacebookLoginHelper(loginNavigation)

    @Provides
    fun provideLoginNavigation(activity: Activity) : LoginNavigation{
        val hostController = Navigation.findNavController(activity, R.id.app_host_fragment)
        return LoginNavigation(hostController)
    }

    @Provides
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun provideViewModel(loginViewModel: LoginViewModel): ViewModel = loginViewModel
}
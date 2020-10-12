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
    fun provideFacebookLoginHelper(activity: Activity) : FacebookLoginHelper{
        val controller = Navigation.findNavController(activity, R.id.app_host_fragment)
        return FacebookLoginHelper(controller)
    }

    @Provides
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun provideViewModel(loginViewModel: LoginViewModel): ViewModel = loginViewModel
}
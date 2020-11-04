package by.aermakova.todoapp.ui.auth.register

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.remote.auth.*
import by.aermakova.todoapp.data.remote.auth.loginManager.EmailLoginManager
import by.aermakova.todoapp.ui.auth.LoginNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class RegisterModule {


    @Provides
    fun provideAuthListener(loginNavigation: LoginNavigation): AuthListener {
        return RegistrationAuthListener(
            loginNavigation
        )
    }

    @Provides
    fun provideAuthListenerImpl(authListener: AuthListener): LoginAuthorizationListener {
        return LoginAuthorizationListenerImpl(
            authListener
        )
    }

    @Provides
    fun provideEmailLoginManager(activity: Activity) =
        EmailLoginManager(
            object :
                LoginStatusListener {
                override fun onSuccess() {
                    Log.d("RegisterModule", "onSuccess")
                }

                override fun onCancel() {
                    Log.d("RegisterModule", "onCancel")
                }

                override fun onError(errorMessage: String?) {
                    Log.d("RegisterModule", "onError")
                }
            },
            activity.resources.getString(R.string.error_loading_cancel)
        )

    @Provides
    fun provideLoginNavigation(activity: Activity): LoginNavigation {
        val hostController = Navigation.findNavController(activity, R.id.app_host_fragment)
        return LoginNavigation(hostController)
    }

    @Provides
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    fun provideViewModel(viewModel: RegisterViewModel): ViewModel = viewModel
}
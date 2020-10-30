package by.aermakova.todoapp.ui.register

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.remote.auth.*
import by.aermakova.todoapp.data.remote.auth.loginManager.EmailLoginManager
import by.aermakova.todoapp.ui.login.*
import com.google.firebase.auth.AuthCredential
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
    fun provideEmailLoginManager() =
        EmailLoginManager(
            object :
                LoginListener {
                override fun onSuccess(credential: AuthCredential?) {
                    credential?.let { FirebaseAuthUtil.signInForDataBase(credential) }
                }

                override fun onCancel() {
                    Log.d("A_LoginModule", "onCancel")
                }

                override fun onError() {
                    Log.d("A_LoginModule", "onError")
                }
            })

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
package by.aermakova.todoapp.ui.login

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.remote.auth.FirebaseAuthUtil
import by.aermakova.todoapp.data.remote.auth.LoginListener
import by.aermakova.todoapp.data.remote.auth.*
import by.aermakova.todoapp.data.remote.auth.loginManager.EmailLoginManager
import by.aermakova.todoapp.data.remote.auth.loginManager.FacebookLoginManager
import com.google.firebase.auth.AuthCredential
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class LoginModule {

    @Provides
    fun provideAuthListener(loginNavigation: LoginNavigation): AuthListener {
        return LoginAuthListener(loginNavigation)
    }

    @Provides
    fun provideAuthListenerImpl(authListener: AuthListener): LoginAuthorizationListener {
        return LoginAuthorizationListenerImpl(
            authListener
        )
    }

    @Provides
    fun provideFacebookLoginManager() =
        FacebookLoginManager(
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
    @ViewModelKey(LoginViewModel::class)
    fun provideViewModel(loginViewModel: LoginViewModel): ViewModel = loginViewModel
}
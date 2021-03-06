package by.aermakova.todoapp.ui.auth.register

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.remote.auth.AuthListener
import by.aermakova.todoapp.data.remote.auth.LoginAuthorizationListener
import by.aermakova.todoapp.data.remote.auth.LoginAuthorizationListenerImpl
import by.aermakova.todoapp.data.remote.auth.RegistrationAuthListener
import by.aermakova.todoapp.data.remote.auth.loginManager.createEmailLoginManager
import by.aermakova.todoapp.ui.auth.LoginMainNavigation
import by.aermakova.todoapp.ui.auth.LoginNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Status
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.Subject

@Module
class RegisterModule {


    @Provides
    fun provideAuthListener(loginNavigation: LoginNavigation): AuthListener {
        return RegistrationAuthListener(
            loginNavigation
        )
    }

    @Provides
    fun provideAuthListenerImpl(authListener: AuthListener): LoginAuthorizationListener
        = LoginAuthorizationListenerImpl(authListener)

    @Provides
    fun provideDisposable() = CompositeDisposable()

    @Provides
    fun provideEmailLoginManager(activity: Activity, command: Subject<Status>) =
        activity.createEmailLoginManager(command)

    @Provides
    fun provideLoginNavigation(controller: NavController) = LoginNavigation(controller)

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    fun provideTasksNavigation(controller: NavController): MainFlowNavigation =
        LoginMainNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    fun provideViewModel(viewModel: RegisterViewModel): ViewModel = viewModel
}
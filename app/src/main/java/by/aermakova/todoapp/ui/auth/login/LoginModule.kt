package by.aermakova.todoapp.ui.auth.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.di.scope.LoginFacebook
import by.aermakova.todoapp.data.di.scope.LoginGoogle
import by.aermakova.todoapp.data.interactor.*
import by.aermakova.todoapp.data.remote.auth.AuthListener
import by.aermakova.todoapp.data.remote.auth.LoginAuthListener
import by.aermakova.todoapp.data.remote.auth.LoginAuthorizationListener
import by.aermakova.todoapp.data.remote.auth.LoginAuthorizationListenerImpl
import by.aermakova.todoapp.data.remote.auth.loginManager.FacebookLoginManager
import by.aermakova.todoapp.data.remote.auth.loginManager.GoogleLoginManager
import by.aermakova.todoapp.data.remote.auth.loginManager.createEmailLoginManager
import by.aermakova.todoapp.data.remote.auth.loginManager.createLoginStatusListener
import by.aermakova.todoapp.data.remote.sync.RemoteDatabaseSynchronization
import by.aermakova.todoapp.ui.auth.LoginNavigation
import by.aermakova.todoapp.util.Status
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.Subject

@Module
class LoginModule {

    @Provides
    fun provideRemoteDataBaseSync(
        goalInteractor: GoalInteractor,
        keyResultInteractor: KeyResultInteractor,
        stepInteractor: StepInteractor,
        taskInteractor: TaskInteractor,
        ideaInteractor: IdeaInteractor
    ): RemoteDatabaseSynchronization {
        return RemoteDatabaseSynchronization(
            goalInteractor,
            keyResultInteractor,
            stepInteractor,
            taskInteractor,
            ideaInteractor
        )
    }

    @Provides
    fun provideDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun provideAuthListener(
        loginNavigation: LoginNavigation,
        fragment: LoginFragment,
        remoteDataBaseSync: RemoteDatabaseSynchronization
    ): AuthListener {
        return LoginAuthListener(loginNavigation, fragment, remoteDataBaseSync)
    }

    @Provides
    fun provideAuthListenerImpl(authListener: AuthListener): LoginAuthorizationListener {
        return LoginAuthorizationListenerImpl(
            authListener
        )
    }

    @Provides
    fun provideFacebookLoginManager(
        activity: Activity,
        command: Subject<Status>
    ): FacebookLoginManager =
        FacebookLoginManager(activity.createLoginStatusListener(command), null)

    @Provides
    fun provideGoogleLoginManager(
        activity: Activity,
        command: Subject<Status>
    ): GoogleLoginManager =
        GoogleLoginManager(activity.createLoginStatusListener(command), null)

    @Provides
    fun provideEmailLoginManager(activity: Activity, command: Subject<Status>) =
        activity.createEmailLoginManager(command)

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
package by.aermakova.todoapp.ui.auth.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.interactor.*
import by.aermakova.todoapp.data.remote.auth.*
import by.aermakova.todoapp.data.remote.auth.loginManager.EmailLoginManager
import by.aermakova.todoapp.data.remote.auth.loginManager.FacebookLoginManager
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
    fun provideFacebookLoginManager(loginStatusListener: LoginStatusListener) =
        FacebookLoginManager(loginStatusListener, null)

    @Provides
    fun provideEmailLoginManager(loginStatusListener: LoginStatusListener, activity: Activity) =
        EmailLoginManager(
            loginStatusListener,
            activity.resources.getString(R.string.error_invalid_email_or_password)
        )

    @Provides
    fun provideLoginStatusListener(activity: Activity, command: Subject<Status>) =
        object : LoginStatusListener {
            override fun onSuccess() {
                command.onNext(Status.SUCCESS)
            }

            override fun onCancel() {
                command.onNext(Status.ERROR.apply {
                    message = activity.resources.getString(R.string.error_loading_cancel)
                })
            }

            override fun onError(errorMessage: String?) {
                command.onNext(Status.ERROR.apply {
                    message = errorMessage ?: ""
                })
            }
        }

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
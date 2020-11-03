package by.aermakova.todoapp.ui.login

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.interactor.*
import by.aermakova.todoapp.data.remote.auth.*
import by.aermakova.todoapp.data.remote.auth.loginManager.EmailLoginManager
import by.aermakova.todoapp.data.remote.auth.loginManager.FacebookLoginManager
import by.aermakova.todoapp.data.remote.sync.RemoteDatabaseSynchronization
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.reactivex.disposables.CompositeDisposable

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
    fun provideFacebookLoginManager() =
        FacebookLoginManager(
            object :
                LoginStatusListener {
                override fun onSuccess() {
                    Log.d("A_LoginModule", "onSuccess")
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
                LoginStatusListener {
                override fun onSuccess() {
                    Log.d("A_LoginModule", "onSuccess")
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
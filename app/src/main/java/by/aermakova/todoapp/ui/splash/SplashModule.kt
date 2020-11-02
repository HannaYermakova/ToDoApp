package by.aermakova.todoapp.ui.splash

import androidx.lifecycle.ViewModel
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.remote.RemoteDataBaseSync
import by.aermakova.todoapp.data.remote.auth.AuthListener
import by.aermakova.todoapp.data.remote.auth.LoginAuthorizationListener
import by.aermakova.todoapp.data.remote.auth.LoginAuthorizationListenerImpl
import by.aermakova.todoapp.data.remote.auth.SplashAuthListener
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class SplashModule {

    @Provides
    fun provideRemoteDataBaseSync(goalInteractor: GoalInteractor): RemoteDataBaseSync {
        return RemoteDataBaseSync(goalInteractor)
    }

    @Provides
    fun provideAuthListener(
        activity: SplashActivity,
        remoteDataBaseSync: RemoteDataBaseSync
    ): AuthListener {
        return SplashAuthListener(activity, remoteDataBaseSync)
    }

    @Provides
    fun provideAuthListenerImpl(authListener: AuthListener): LoginAuthorizationListener {
        return LoginAuthorizationListenerImpl(
            authListener
        )
    }

    @Provides
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    fun provideViewModule(viewModel: SplashViewModel): ViewModel = viewModel
}
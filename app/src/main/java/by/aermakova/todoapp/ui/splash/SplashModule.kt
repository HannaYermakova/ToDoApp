package by.aermakova.todoapp.ui.splash

import androidx.lifecycle.ViewModel
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.interactor.*
import by.aermakova.todoapp.data.remote.sync.RemoteDatabaseSynchronization
import by.aermakova.todoapp.data.remote.auth.AuthListener
import by.aermakova.todoapp.data.remote.auth.LoginAuthorizationListener
import by.aermakova.todoapp.data.remote.auth.LoginAuthorizationListenerImpl
import by.aermakova.todoapp.data.remote.auth.SplashAuthListener
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.reactivex.disposables.CompositeDisposable

@Module
class SplashModule {

    @Provides
    fun provideAuthListener(
        activity: SplashActivity
    ): AuthListener {
        return SplashAuthListener(activity)
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
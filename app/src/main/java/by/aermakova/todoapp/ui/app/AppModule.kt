package by.aermakova.todoapp.ui.app

import androidx.lifecycle.ViewModel
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.interactor.*
import by.aermakova.todoapp.data.remote.auth.AppAuthListener
import by.aermakova.todoapp.data.remote.auth.AuthListener
import by.aermakova.todoapp.data.remote.auth.LoginAuthorizationListener
import by.aermakova.todoapp.data.remote.auth.LoginAuthorizationListenerImpl
import by.aermakova.todoapp.data.remote.sync.RemoteDatabaseSynchronization
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.reactivex.disposables.CompositeDisposable

@Module
class AppModule {

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
    fun provideDisposable() = CompositeDisposable()

    @Provides
    fun provideAuthListener(
        activity: AppActivity,
        remoteDataBaseSync: RemoteDatabaseSynchronization
    ): AuthListener {
        return AppAuthListener(
            activity,
            remoteDataBaseSync
        )
    }

    @Provides
    fun provideAuthListenerImpl(authListener: AuthListener): LoginAuthorizationListener =
        LoginAuthorizationListenerImpl(authListener)


    @Provides
    @IntoMap
    @ViewModelKey(AppViewModel::class)
    fun provideViewModel(viewModel: AppViewModel): ViewModel = viewModel
}
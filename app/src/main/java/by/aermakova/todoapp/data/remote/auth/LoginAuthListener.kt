package by.aermakova.todoapp.data.remote.auth

import by.aermakova.todoapp.data.remote.sync.RemoteDatabaseSynchronization
import by.aermakova.todoapp.ui.auth.login.LoginFragment
import by.aermakova.todoapp.ui.auth.LoginNavigation


class LoginAuthListener(
    private val loginNavigation: LoginNavigation,
    private val fragment: LoginFragment,
    private val remoteDataBaseSync: RemoteDatabaseSynchronization
) :
    AuthListener {

    private var firstLogin = false

    override fun isSignIn() {
        if (firstLogin) {
            remoteDataBaseSync.startSync(fragment.compositeDisposable) {
                loginNavigation.navigateAfterLoginSuccess()
            }
        } else {
            loginNavigation.navigateAfterLoginSuccess()
        }
    }

    override fun notSignIn() {
        firstLogin = true
    }
}
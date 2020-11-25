package by.aermakova.todoapp.data.remote.auth

import android.util.Log
import by.aermakova.todoapp.data.remote.sync.RemoteDatabaseSynchronization
import by.aermakova.todoapp.ui.app.AppActivity
import by.aermakova.todoapp.ui.auth.login.LoginFragment
import by.aermakova.todoapp.ui.auth.LoginNavigation


class AppAuthListener(
    private val activity: AppActivity,
    private val remoteDataBaseSync: RemoteDatabaseSynchronization
) :
    AuthListener {

    private var firstLogin = false

    override fun isSignIn() {
        Log.d("AppAuthListener", "isSignIn first login: $firstLogin")
        if (!firstLogin) {
            remoteDataBaseSync.startSync(activity.disposable) {}
        }
    }

    override fun notSignIn() {
        Log.d("AppAuthListener", "not Sign In")
        firstLogin = true
    }
}
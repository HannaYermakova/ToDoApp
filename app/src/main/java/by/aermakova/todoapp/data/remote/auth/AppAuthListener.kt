package by.aermakova.todoapp.data.remote.auth

import by.aermakova.todoapp.data.remote.sync.RemoteDatabaseSynchronization
import by.aermakova.todoapp.ui.app.AppActivity

class AppAuthListener(
    private val activity: AppActivity,
    private val remoteDataBaseSync: RemoteDatabaseSynchronization
) :
    AuthListener {

    private var firstLogin = false

    override fun isSignIn() {
        if (!firstLogin) {
            remoteDataBaseSync.startSync(activity.disposable) {}
        }
    }

    override fun notSignIn() {
        firstLogin = true
    }
}
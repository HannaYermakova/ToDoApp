package by.aermakova.todoapp.data.remote.auth.loginManager

import android.app.Activity
import android.content.Intent
import android.util.Log
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.remote.auth.EmailCredentials
import by.aermakova.todoapp.data.remote.auth.FirebaseAuthUtil
import by.aermakova.todoapp.data.remote.auth.LoginStatusListener
import by.aermakova.todoapp.util.Status
import io.reactivex.Observer

class EmailLoginManager(
    private val loginListener: LoginStatusListener,
    private val message: String?
) :
    AppLoginManager {

    override val errorMessage: String?
        get() = message

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("A_EmailLoginManager", "onActivityResult")
    }

    fun signInWithEmailAndPassword(emailCredentials: EmailCredentials) {
        FirebaseAuthUtil.signInWithEmailAndPassword(
            emailCredentials,
            loginListener
        )
    }

    fun createAccount(emailCredentials: EmailCredentials) {
        FirebaseAuthUtil.createUserWithEmailAndPassword(
            emailCredentials,
            loginListener
        )
    }
}


fun Activity.createLoginStatusListener(command: Observer<Status>): LoginStatusListener {
    return object :
        LoginStatusListener {
        override fun onSuccess() {
            command.onNext(Status.LOADING)
        }

        override fun onCancel() {
            command.onNext(Status.ERROR.apply {
                message = getString(R.string.error_loading_cancel)
            })
        }

        override fun onError(errorMessage: String?) {
            command.onNext(Status.ERROR.apply {
                message = errorMessage ?: ""
            })
        }
    }
}

fun Activity.createEmailLoginManager(command: Observer<Status>): EmailLoginManager{
    return EmailLoginManager(createLoginStatusListener(command), getString(R.string.error_loading_cancel))
}
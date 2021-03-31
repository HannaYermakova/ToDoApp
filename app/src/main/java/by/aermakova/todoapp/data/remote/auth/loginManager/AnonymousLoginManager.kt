package by.aermakova.todoapp.data.remote.auth.loginManager

import android.content.Intent
import android.util.Log
import by.aermakova.todoapp.data.remote.auth.FirebaseAuthUtil
import by.aermakova.todoapp.data.remote.auth.LoginStatusListener

class AnonymousLoginManager(
    private val loginListener: LoginStatusListener,
    private val message: String?
) : AppLoginManager {

    override val errorMessage: String?
        get() = message

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("A_AnonymousLoginManager", "onActivityResult")
    }

    fun signInAnonymously() {
        FirebaseAuthUtil.signInAnonymously(
            loginListener
        )
    }
}
package by.aermakova.todoapp.data.remote.auth.loginManager

import android.content.Intent
import android.util.Log
import by.aermakova.todoapp.data.remote.auth.FirebaseAuthUtil
import by.aermakova.todoapp.data.remote.auth.LoginStatusListener

class EmailLoginManager(
    private val loginListener: LoginStatusListener,
    private val message: String?
) :
    AppLoginManager {

    override val errorMessage: String?
        get() = message

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        Log.d("A_EmailLoginManager", "onActivityResult")
        return true
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        FirebaseAuthUtil.signInWithEmailAndPassword(
            email,
            password,
            loginListener
        )
    }

    fun createAccount(email: String, password: String) {
        FirebaseAuthUtil.createUserWithEmailAndPassword(
            email,
            password,
            loginListener
        )
    }
}
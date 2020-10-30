package by.aermakova.todoapp.data.remote.auth

import com.google.firebase.auth.FirebaseAuth

interface LoginAuthorizationListener {

    fun registerListener()
    fun unregisterListener()
}

class LoginAuthorizationListenerImpl(
    authListener: AuthListener
) :
    LoginAuthorizationListener {

    private var authStateListener: FirebaseAuth.AuthStateListener =
        FirebaseAuthUtil.getAuthStateListener(authListener)

    override fun registerListener() {
        FirebaseAuthUtil.addListener(authStateListener)
    }

    override fun unregisterListener() {
        FirebaseAuthUtil.removeListener(authStateListener)
    }
}
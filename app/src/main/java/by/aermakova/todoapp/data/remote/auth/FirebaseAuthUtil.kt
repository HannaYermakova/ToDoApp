package by.aermakova.todoapp.data.remote.auth

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object FirebaseAuthUtil {

    private val authInstance: FirebaseAuth?
        get() = Firebase.auth

    fun getUid(): String? {
        return authInstance?.let {
            it.currentUser?.uid
        }
    }

    fun addListener(authListener: AuthStateListener) {
        authInstance?.addAuthStateListener(authListener)
    }

    fun removeListener(authListener: AuthStateListener) {
        authInstance?.removeAuthStateListener(authListener)
    }

    fun getAuthStateListener(authListener: AuthListener): AuthStateListener =
        AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            user?.let { authListener.isSignIn() } ?: authListener.notSignIn()
        }

    fun signInForDataBase(credential: AuthCredential, loginListener: LoginStatusListener) {
        authInstance?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful && authInstance?.currentUser != null) {
                    Log.d("A_FirebaseAuthUtil", "succeeded to sign in")
                    loginListener.onSuccess()
                } else {
                    Log.d("A_FirebaseAuthUtil", "failed to sign in. " + task.exception)
                    loginListener.onError()
                }
            }
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        loginListener: LoginStatusListener
    ) {
        authInstance?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("A_FirebaseAuthUtil", "succeeded to sign in")
                    val credential = EmailAuthProvider.getCredential(email, password)
                    signInForDataBase(credential, loginListener)
                } else {
                    Log.d("A_FirebaseAuthUtil", "failed to sign in. " + task.exception)
                    loginListener.onError()
                }
            }
    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        loginListener: LoginStatusListener
    ) {
        authInstance?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("A_FirebaseAuthUtil", "succeeded to register")
                    val credential = EmailAuthProvider.getCredential(email, password)
                    signInForDataBase(credential, loginListener)
                } else {
                    Log.d("A_FirebaseAuthUtil", "failed to register. " + task.exception)
                    loginListener.onError()
                }
            }
    }
}
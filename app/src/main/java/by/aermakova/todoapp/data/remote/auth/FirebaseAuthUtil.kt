package by.aermakova.todoapp.data.remote.auth

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
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

    fun exit() {
        authInstance?.signOut()
    }

    fun addListener(authListener: AuthStateListener) {
        authInstance?.addAuthStateListener(authListener)
    }

    fun removeListener(authListener: AuthStateListener) {
        authInstance?.removeAuthStateListener(authListener)
    }

    fun getAuthStateListener(authListener: AuthListener): AuthStateListener =
        AuthStateListener { firebaseAuth ->
            Log.d("A_FirebaseAuthUtil", "get auth state listener")
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
                    loginListener.onError(task.exception?.message)
                }
            }
    }

    fun signInWithEmailAndPassword(
        emailCredentials: EmailCredentials,
        loginListener: LoginStatusListener
    ) {
        useEmailCredentials(emailCredentials, loginListener) { email, password ->
            authInstance?.signInWithEmailAndPassword(email, password)
        }
    }

    fun createUserWithEmailAndPassword(
        emailCredentials: EmailCredentials,
        loginListener: LoginStatusListener
    ) {
        useEmailCredentials(emailCredentials, loginListener) { email, password ->
            authInstance?.createUserWithEmailAndPassword(email, password)
        }
    }

    private fun useEmailCredentials(
        emailCredentials: EmailCredentials,
        loginListener: LoginStatusListener,
        signIn: (String, String) -> Task<AuthResult>?
    ) {
        with(emailCredentials) {
            signIn.invoke(email, password)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("A_FirebaseAuthUtil", "succeeded to sign in")
                    val credential = EmailAuthProvider.getCredential(email, password)
                    signInForDataBase(credential, loginListener)
                } else {
                    Log.d("A_FirebaseAuthUtil", "failed to sign in. " + task.exception)
                    loginListener.onError(task.exception?.message)
                }
            }
        }
    }

    fun firebaseAuthWithGoogle(idToken: String, loginListener: LoginStatusListener) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        authInstance?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("A_FirebaseAuthUtil", "firebaseAuthWithGoogle ok")
                    signInForDataBase(credential, loginListener)
                } else {
                    Log.d("A_FirebaseAuthUtil", "firebaseAuthWithGoogle error")
                    loginListener.onError(task.exception?.message)
                }
            }
    }

    fun signInAnonymously(loginListener: LoginStatusListener){
        Log.d("A_FirebaseAuthUtil","signInAnonymously")
        authInstance?.signInAnonymously()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("A_FirebaseAuthUtil","signInAnonymously:success")
                    loginListener.onSuccess()
                } else {
                    Log.d("A_FirebaseAuthUtil","signInAnonymously:failure", task.exception)
                    loginListener.onError(task.exception?.message)
                }
            }
    }
}
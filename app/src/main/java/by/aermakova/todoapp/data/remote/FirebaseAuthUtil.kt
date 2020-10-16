package by.aermakova.todoapp.data.remote

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object FirebaseAuthUtil {

    private val authInstance: FirebaseAuth? = Firebase.auth

    fun getUid(): String? {
        authInstance?.let {
            return it.currentUser?.uid
        }
        return null
    }

    fun signInWithCredential(credential: AuthCredential) {
        authInstance?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful && authInstance.currentUser != null) {
                    val currentUser: FirebaseUser? = authInstance.currentUser
                    Log.d("A_FirebaseAuthUtil", "succeeded to sign in with Google. $currentUser")
                } else {
                    Log.d("A_FirebaseAuthUtil","failed to sign in. " + task.exception)
                }
            }
    }
}
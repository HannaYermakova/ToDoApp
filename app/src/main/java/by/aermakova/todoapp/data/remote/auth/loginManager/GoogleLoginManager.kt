package by.aermakova.todoapp.data.remote.auth.loginManager

import android.app.Activity
import android.content.Intent
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.remote.auth.FirebaseAuthUtil.firebaseAuthWithGoogle
import by.aermakova.todoapp.data.remote.auth.LoginStatusListener
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class GoogleLoginManager(
    private val loginListener: LoginStatusListener,
    private val message: String?
) : AppLoginManager {

    companion object {
        const val RC_SIGN_IN = 4147
    }

    override val errorMessage: String?
        get() = message

    fun signIn(activity: Activity) {
        val providers = arrayListOf(
            IdpConfig.GoogleBuilder().build())
        activity.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_goals)
                .setIsSmartLockEnabled(true)
                .build(),
            RC_SIGN_IN)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account?.idToken!!, loginListener)
        } catch (e: ApiException) {
            loginListener.onError(errorMessage)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
}
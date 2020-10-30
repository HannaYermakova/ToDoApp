package by.aermakova.todoapp.data.remote.auth.loginManager

import android.content.Intent
import androidx.fragment.app.Fragment
import by.aermakova.todoapp.data.remote.auth.LoginListener
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider

class FacebookLoginManager(private val loginListener: LoginListener) :
    AppLoginManager {

    companion object {
        private const val FACEBOOK_PERMISSION_EMAIL = "email"
        private const val FACEBOOK_PERMISSION_PUBLIC_PROFILE = "public_profile"
    }

    private val _fbCallbackManager = CallbackManager.Factory.create()

    fun signInWithFacebookAccount(fragment: Fragment) {
        LoginManager.getInstance().logInWithReadPermissions(
            fragment,
            listOf(
                FACEBOOK_PERMISSION_EMAIL,
                FACEBOOK_PERMISSION_PUBLIC_PROFILE
            )
        )
        registerFacebookLoginListener()
    }

    private fun registerFacebookLoginListener() {
        LoginManager.getInstance().registerCallback(_fbCallbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    loginResult?.let {
                        val credential = FacebookAuthProvider.getCredential(it.accessToken.token)
                        loginListener.onSuccess(credential)
                    }
                }

                override fun onCancel() {
                    loginListener.onCancel()
                }

                override fun onError(exception: FacebookException) {
                    loginListener.onError()
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return _fbCallbackManager.onActivityResult(requestCode, resultCode, data)
    }
}
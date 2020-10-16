package by.aermakova.todoapp.ui.login

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import by.aermakova.todoapp.data.remote.FirebaseAuthUtil
import by.aermakova.todoapp.ui.navigation.AuthFlowNavigation
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider

private const val FACEBOOK_PERMISSION_EMAIL = "email"
private const val FACEBOOK_PERMISSION_PUBLIC_PROFILE = "public_profile"

class FacebookLoginHelper(private val authFlowNavigation: AuthFlowNavigation) {

    private val _fbCallbackManager = CallbackManager.Factory.create()
    val fbCallbackManager: CallbackManager
        get() = _fbCallbackManager

    fun setFacebookListener(loginButton: LoginButton, loginFragment: Fragment) {
        with(loginButton) {
            setReadPermissions(FACEBOOK_PERMISSION_EMAIL, FACEBOOK_PERMISSION_PUBLIC_PROFILE)
            fragment = loginFragment
        }
        registerFacebookLoginListener()
    }

    private fun registerFacebookLoginListener() {
        LoginManager.getInstance().registerCallback(fbCallbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    Log.i("LoginViewModel", "onSuccess")
                    loginResult?.let {
                        val credential: AuthCredential = FacebookAuthProvider.getCredential(it.accessToken.token)
                        setCredentialForDataBase(credential)
                        authFlowNavigation.navigateAfterLoginSuccess()
                    }
                }

                override fun onCancel() {
                    Log.i("LoginViewModel", "onCancel")
                }

                override fun onError(exception: FacebookException) {
                    Log.i("LoginViewModel", "onError")
                }
            })
    }

    private fun setCredentialForDataBase(credential: AuthCredential) {
        FirebaseAuthUtil.signInWithCredential(credential)
    }
}
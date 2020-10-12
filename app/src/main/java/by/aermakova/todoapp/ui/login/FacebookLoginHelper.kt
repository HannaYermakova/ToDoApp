package by.aermakova.todoapp.ui.login

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import by.aermakova.todoapp.R
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton

private const val FACEBOOK_PERMISSION_EMAIL = "email"
private const val FACEBOOK_PERMISSION_PUBLIC_PROFILE = "public_profile"

class FacebookLoginHelper(private val hostController: NavController){

    private val _fbCallbackManager = CallbackManager.Factory.create()
    val fbCallbackManager : CallbackManager
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
                    hostController.navigate(R.id.action_authFlowFragment_to_mainFlowFragment)
                    Log.d("LoginViewModel", "facebook:onSuccess:$loginResult");
                }

                override fun onCancel() {
                    Log.i("LoginViewModel", "onCancel")

                }

                override fun onError(exception: FacebookException) {
                    Log.i("LoginViewModel", "onError")
                }
            })
    }
}
package by.aermakova.todoapp.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import by.aermakova.todoapp.R
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

class LoginViewModel : ViewModel() {

    fun registerFacebookLoginListener(fbCallbackManager: CallbackManager, hostController: NavController) {
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
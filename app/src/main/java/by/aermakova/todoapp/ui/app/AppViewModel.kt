package by.aermakova.todoapp.ui.app

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import by.aermakova.todoapp.R
import com.facebook.AccessToken

class AppViewModel : ViewModel() {

    fun checkLogin(controller: NavController) {
        if (checkFacebookLogin()) {
            Log.i("AppViewModel", "checkLogin")
            controller.navigate(R.id.action_authFlowFragment_to_mainFlowFragment)
        }
    }

    private fun checkFacebookLogin(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }
}
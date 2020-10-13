package by.aermakova.todoapp.ui.login

import android.util.Log
import androidx.navigation.NavController
import by.aermakova.todoapp.R
import by.aermakova.todoapp.ui.navigation.AuthFlowNavigation

class LoginNavigation(private val hostController: NavController) : AuthFlowNavigation {

    override fun navigateAfterLoginSuccess() {
        hostController.navigate(R.id.action_authFlowFragment_to_mainFlowFragment)
    }

    override fun navigateToRegisterFragment() {
        Log.i("LoginNavigation", "navigateToRegisterFragment")
    }
}
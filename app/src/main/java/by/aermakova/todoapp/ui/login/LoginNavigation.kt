package by.aermakova.todoapp.ui.login

import androidx.navigation.NavController
import by.aermakova.todoapp.R
import by.aermakova.todoapp.ui.navigation.AuthNavigation

class LoginNavigation(private val hostController: NavController) : AuthNavigation {

    override fun navigateAfterLoginSuccess() {
        hostController.navigate(R.id.action_loginFragment_to_mainFlowFragment)
    }

    override fun navigateAfterRegisterSuccess() {
        hostController.navigate(R.id.action_registerFragment_to_mainFlowFragment)
    }

    override fun navigateToRegisterFragment() {
        hostController.navigate(R.id.action_loginFragment_to_registerFragment)
    }
}
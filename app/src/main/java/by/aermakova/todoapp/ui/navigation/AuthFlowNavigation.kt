package by.aermakova.todoapp.ui.navigation

import android.view.View

interface AuthFlowNavigation {

    interface Settings{
        fun attachNavigationControllerToView(view: View)
    }

    fun navigateAfterLoginSuccess()

    fun navigateToRegisterFragment()

}
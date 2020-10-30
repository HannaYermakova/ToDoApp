package by.aermakova.todoapp.ui.navigation

import android.view.View

interface AuthNavigation {

    interface Settings{
        fun attachNavigationControllerToView(view: View)
    }

    fun navigateAfterLoginSuccess()

    fun navigateAfterRegisterSuccess()

    fun navigateToRegisterFragment()

}
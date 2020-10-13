package by.aermakova.todoapp.ui.auth

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.ui.navigation.AuthFlowNavigation

class AuthFlowNavigationSettings(private val controller: NavController) :
    AuthFlowNavigation.Settings {

    override fun attachNavigationControllerToView(view: View) {
        Navigation.setViewNavController(view, controller)
    }
}
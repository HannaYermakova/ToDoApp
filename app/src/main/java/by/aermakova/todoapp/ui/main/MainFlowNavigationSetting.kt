package by.aermakova.todoapp.ui.main

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainFlowNavigationSetting(private val controller: NavController) : MainFlowNavigation.Settings {

    override fun attachNavigationControllerToNavView(navigationView: BottomNavigationView) {
        Navigation.setViewNavController(navigationView.rootView, controller)
        NavigationUI.setupWithNavController(navigationView, controller)
    }
}
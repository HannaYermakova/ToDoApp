package by.aermakova.todoapp.ui.navigation

import com.google.android.material.bottomnavigation.BottomNavigationView

interface MainFlowNavigation {

    interface Settings{
        fun attachNavigationControllerToNavView(navigationView: BottomNavigationView)
    }

    fun navigateToAddNewElementFragment()

    fun navigateToAddNewElementFragment(goalId: Long = -1L)

    fun navigateToShowDetailsFragment(id: Long)

    fun popBack()
}
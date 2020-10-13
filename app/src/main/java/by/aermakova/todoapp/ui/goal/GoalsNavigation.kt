package by.aermakova.todoapp.ui.goal

import android.util.Log
import androidx.navigation.NavController
import by.aermakova.todoapp.R
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation

class GoalsNavigation(private val controller: NavController) : MainFlowNavigation {

    override fun navigateToAddNewElementFragment() {
        controller.navigate(R.id.action_mainFlowFragment_to_addGoalFragment)
    }

    override fun navigateToShowDetailsFragment(id: Long) {
        Log.i("GoalsNavigation", "showDetailsFragment")
    }

    override fun popBack() {
        controller.popBackStack()
    }
}
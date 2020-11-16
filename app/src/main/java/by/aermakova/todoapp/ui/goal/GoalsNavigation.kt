package by.aermakova.todoapp.ui.goal

import android.util.Log
import androidx.navigation.NavController
import by.aermakova.todoapp.R
import by.aermakova.todoapp.ui.main.MainFlowFragmentDirections
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation

class GoalsNavigation(private val controller: NavController) : MainFlowNavigation {

    override fun navigateToAddNewElementFragment() {
        controller.navigate(R.id.action_mainFlowFragment_to_addGoalFragment)
    }

    override fun navigateToAddNewElementFragment(goalId: Long) {
        try {
            controller.navigate(
                MainFlowFragmentDirections.actionMainFlowFragmentToAddTaskFragment(
                    goalId
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun navigateToEditElementFragment(id: Long) {
        try {
            controller.navigate(
                MainFlowFragmentDirections.actionMainFlowFragmentToEditGoalFragment(
                    id
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun navigateToShowDetailsFragment(id: Long) {
        controller.navigate(
            MainFlowFragmentDirections.actionMainFlowFragmentToGoalDetailsFragment(
                id
            )
        )
    }

    override fun popBack() {
        controller.popBackStack()
    }

    fun exit() {
        controller.navigate(MainFlowFragmentDirections.actionMainFlowFragmentToLoginFragment())
    }
}
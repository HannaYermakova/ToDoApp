package by.aermakova.todoapp.ui.goal

import androidx.navigation.NavController
import by.aermakova.todoapp.AppNavigationDirections
import by.aermakova.todoapp.R
import by.aermakova.todoapp.ui.main.MainFlowFragmentDirections
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Item

class GoalsNavigation(private val controller: NavController) : MainFlowNavigation {

    override fun navigateToAddNewElementFragment() {
        controller.navigate(R.id.action_mainFlowFragment_to_addGoalFragment)
    }

    override fun navigateToAddNewElementFragment(itemId: Long, item: Item) {
        try {
            controller.navigate(
                MainFlowFragmentDirections.actionMainFlowFragmentToAddGoalFragment()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun navigateToEditElementFragment(id: Long) {
        try {
            controller.navigate(AppNavigationDirections.actionGlobalEditGoalFragment(id))
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
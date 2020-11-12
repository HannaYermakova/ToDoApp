package by.aermakova.todoapp.ui.idea

import androidx.navigation.NavController
import by.aermakova.todoapp.R
import by.aermakova.todoapp.ui.main.MainFlowFragmentDirections
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation

class IdeasNavigation(private val controller: NavController) : MainFlowNavigation {

    override fun navigateToAddNewElementFragment() {
        controller.navigate(R.id.action_mainFlowFragment_to_addIdeaFragment)
    }

    override fun navigateToAddNewElementFragment(goalId: Long) {
        try {
            controller.navigate(
                MainFlowFragmentDirections.actionMainFlowFragmentToAddIdeaFragment(goalId)
            )
        } catch (exception: IllegalArgumentException) {
            exception.printStackTrace()
        }
    }

    override fun navigateToShowDetailsFragment(id: Long) {
        controller.navigate(
            MainFlowFragmentDirections.actionMainFlowFragmentToIdeaDetailsFragment(
                id
            )
        )
    }

    override fun popBack() {
        controller.popBackStack(R.id.mainFlowFragment, false)
    }
}
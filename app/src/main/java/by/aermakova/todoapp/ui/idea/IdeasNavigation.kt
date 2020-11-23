package by.aermakova.todoapp.ui.idea

import androidx.navigation.NavController
import by.aermakova.todoapp.AppNavigationDirections
import by.aermakova.todoapp.R
import by.aermakova.todoapp.ui.main.MainFlowFragmentDirections
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Item

class IdeasNavigation(private val controller: NavController) : MainFlowNavigation {

    override fun navigateToAddNewElementFragment() {
        controller.navigate(R.id.action_mainFlowFragment_to_addIdeaFragment)
    }

    override fun navigateToAddNewElementFragment(itemId: Long, item: Item) {
        try {
            controller.navigate(
                MainFlowFragmentDirections.actionMainFlowFragmentToAddIdeaFragment(itemId, item.code)
            )
        } catch (exception: IllegalArgumentException) {
            exception.printStackTrace()
        }
    }

    override fun navigateToEditElementFragment(id: Long) {
        try {
            controller.navigate(
                AppNavigationDirections.actionGlobalEditIdeaFragment(id)
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
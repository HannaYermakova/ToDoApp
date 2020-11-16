package by.aermakova.todoapp.ui.step

import androidx.navigation.NavController
import by.aermakova.todoapp.R
import by.aermakova.todoapp.ui.main.MainFlowFragmentDirections
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Item

class StepsNavigation(private val controller: NavController) : MainFlowNavigation {

    override fun navigateToAddNewElementFragment() {
        controller.navigate(R.id.action_mainFlowFragment_to_addStepFragment)
    }

    override fun navigateToAddNewElementFragment(itemId: Long, item: Item) {
        try {
            controller.navigate(
                MainFlowFragmentDirections.actionMainFlowFragmentToAddStepFragment(
                    itemId, item.code
                )
            )
        } catch (exception: IllegalArgumentException) {
            exception.printStackTrace()
        }
    }

    override fun navigateToEditElementFragment(id: Long) {

    }

    override fun navigateToShowDetailsFragment(id: Long) {
        controller.navigate(
            MainFlowFragmentDirections.actionMainFlowFragmentToStepDetailsFragment(
                id
            )
        )
    }

    override fun popBack() {
        controller.popBackStack()
    }
}
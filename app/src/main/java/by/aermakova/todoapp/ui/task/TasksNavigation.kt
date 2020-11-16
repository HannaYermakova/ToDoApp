package by.aermakova.todoapp.ui.task

import androidx.navigation.NavController
import by.aermakova.todoapp.ui.main.MainFlowFragmentDirections
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Item

class TasksNavigation(private val controller: NavController) : MainFlowNavigation {

    override fun navigateToAddNewElementFragment() {
        controller.navigate(MainFlowFragmentDirections.actionGlobalAddTaskFragment())
    }

    override fun navigateToAddNewElementFragment(itemId: Long, item: Item) {
        try {
            controller.navigate(MainFlowFragmentDirections.actionGlobalAddTaskFragment(itemId, item.code))
        } catch (exception : IllegalArgumentException){
            exception.printStackTrace()
        }
    }

    override fun navigateToEditElementFragment(id: Long) {

    }

    override fun navigateToShowDetailsFragment(id: Long) {
        controller.navigate(MainFlowFragmentDirections.actionMainFlowFragmentToTaskDetailsFragment(id))
    }

    override fun popBack() {
        controller.popBackStack()
    }
}
package by.aermakova.todoapp.ui.task

import androidx.navigation.NavController
import by.aermakova.todoapp.R
import by.aermakova.todoapp.ui.main.MainFlowFragmentDirections
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import java.lang.IllegalArgumentException

class TasksNavigation(private val controller: NavController) : MainFlowNavigation {

    override fun navigateToAddNewElementFragment() {
        controller.navigate(R.id.action_mainFlowFragment_to_addTaskFragment)
    }

    override fun navigateToAddNewElementFragment(goalId: Long) {
        try {
            controller.navigate(MainFlowFragmentDirections.actionMainFlowFragmentToAddTaskFragment(goalId))
        } catch (exception : IllegalArgumentException){
            exception.printStackTrace()
        }
    }

    override fun navigateToShowDetailsFragment(id: Long) {
        controller.navigate(MainFlowFragmentDirections.actionMainFlowFragmentToTaskDetailsFragment(id))
    }

    override fun popBack() {
        controller.popBackStack()
    }
}
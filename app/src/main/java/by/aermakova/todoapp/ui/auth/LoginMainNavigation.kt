package by.aermakova.todoapp.ui.auth

import androidx.navigation.NavController
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Item

class LoginMainNavigation(private val controller: NavController) : MainFlowNavigation {

    override fun navigateToAddNewElementFragment() {}

    override fun navigateToAddNewElementFragment(itemId: Long, item: Item) {}

    override fun navigateToEditElementFragment(id: Long) {}

    override fun navigateToShowDetailsFragment(id: Long) {}

    override fun popBack() {
        controller.popBackStack()
    }
}
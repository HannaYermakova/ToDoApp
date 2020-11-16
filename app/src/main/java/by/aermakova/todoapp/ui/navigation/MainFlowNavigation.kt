package by.aermakova.todoapp.ui.navigation

import by.aermakova.todoapp.util.ITEM_IS_NOT_SELECTED_ID
import by.aermakova.todoapp.util.Item
import com.google.android.material.bottomnavigation.BottomNavigationView

interface MainFlowNavigation {

    interface Settings{
        fun attachNavigationControllerToNavView(navigationView: BottomNavigationView)
    }

    fun navigateToAddNewElementFragment()

    fun navigateToAddNewElementFragment(itemId: Long = ITEM_IS_NOT_SELECTED_ID, item: Item)

    fun navigateToEditElementFragment(id: Long)

    fun navigateToShowDetailsFragment(id: Long)

    fun popBack()
}
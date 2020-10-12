package by.aermakova.todoapp.ui.goal

import android.util.Log
import androidx.navigation.NavController
import by.aermakova.todoapp.R
import by.aermakova.todoapp.ui.navigation.FragmentNavigation

class GoalsNavigation(private val controller: NavController) : FragmentNavigation {

    override fun addNewElementFragment() {
        controller.navigate(R.id.action_navigation_goals_to_addGoalFragment)
    }

    override fun showDetailsFragment(id: Long) {
        Log.i("GoalsNavigation", "showDetailsFragment")
    }

    override fun popBack() {
        controller.popBackStack()
    }
}
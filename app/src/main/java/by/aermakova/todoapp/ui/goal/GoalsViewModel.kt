package by.aermakova.todoapp.ui.goal

import android.util.Log
import androidx.lifecycle.ViewModel
import by.aermakova.todoapp.ui.navigation.FragmentNavigation
import javax.inject.Inject

class GoalsViewModel @Inject constructor(
    private val navigation: FragmentNavigation
) : ViewModel() {

    fun checkGoalsViewModel() {
        Log.i("GoalsViewModel", "navigation != null")
    }

    val addNewElement = { navigation.addNewElementFragment() }
}
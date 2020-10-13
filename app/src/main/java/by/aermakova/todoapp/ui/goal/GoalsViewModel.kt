package by.aermakova.todoapp.ui.goal

import android.util.Log
import androidx.lifecycle.ViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import javax.inject.Inject

class GoalsViewModel @Inject constructor(
    private val navigation: MainFlowNavigation
) : ViewModel() {

    fun checkGoalsViewModel() {
        Log.i("GoalsViewModel", "navigation != null")
    }

    val addNewElement = { navigation.navigateToAddNewElementFragment() }
}
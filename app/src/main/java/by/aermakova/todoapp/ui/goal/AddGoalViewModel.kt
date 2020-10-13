package by.aermakova.todoapp.ui.goal

import androidx.lifecycle.ViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import javax.inject.Inject

class AddGoalViewModel @Inject constructor(
    private val navigation: MainFlowNavigation
) : ViewModel() {

    val popBack = { navigation.popBack() }

}
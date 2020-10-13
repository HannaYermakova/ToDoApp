package by.aermakova.todoapp.ui.goal

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import javax.inject.Inject

class AddGoalViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    private val dialogNavigation: DialogNavigation
) : ViewModel() {

    val popBack = { mainFlowNavigation.popBack() }

    val addKeyResult: (String) -> Unit = {
        dialogNavigation.openAddItemDialog(it)
    }

    val saveGoal = { }

    val keyResultObserver: LiveData<String>?
        get() = dialogNavigation.getDialogResult()
}
package by.aermakova.todoapp.ui.dialog

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import by.aermakova.todoapp.AppNavigationDirections
import by.aermakova.todoapp.ui.goal.AddGoalFragmentDirections
import by.aermakova.todoapp.ui.navigation.DialogNavigation

private const val DIALOG_RESULT = "dialog_result"

class AddItemDialogNavigation(private val controller: NavController) : DialogNavigation<String> {

    override fun openAddItemDialog(title: String) {
        controller.navigate(AppNavigationDirections.actionGlobalNameItemDialog(title))
    }

    override fun getDialogResult(): MutableLiveData<String>? {
        return controller.currentBackStackEntry?.savedStateHandle?.getLiveData<String>(DIALOG_RESULT)
    }

    override fun setDialogResult(text: String) {
        controller.previousBackStackEntry?.savedStateHandle?.set(DIALOG_RESULT, text)
    }
}
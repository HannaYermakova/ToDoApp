package by.aermakova.todoapp.ui.dialog.confirm

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import by.aermakova.todoapp.AppNavigationDirections
import by.aermakova.todoapp.ui.navigation.DialogNavigation

class ConfirmDialogNavigation(private val controller: NavController) : DialogNavigation<Boolean> {

    companion object{
        private const val DIALOG_RESULT = "confirm_dialog_result"
    }

    override fun openItemDialog(title: String) {
        controller.navigate(AppNavigationDirections.actionGlobalConfirmDialogFragment(title))
    }

    override fun getDialogResult(): MutableLiveData<Boolean>? {
        return controller.currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(DIALOG_RESULT)
    }

    override fun setDialogResult(result: Boolean) {
        controller.previousBackStackEntry?.savedStateHandle?.set(DIALOG_RESULT, result)
    }
}
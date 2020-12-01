package by.aermakova.todoapp.ui.dialog.confirm

import androidx.navigation.NavController
import by.aermakova.todoapp.AppNavigationDirections
import by.aermakova.todoapp.ui.navigation.DialogNavigation

class ConfirmDialogNavigation(
    private val controller: NavController,
    private val tag: String
) : DialogNavigation<Boolean> {

    override fun openItemDialog(title: String) {
        controller.navigate(AppNavigationDirections.actionGlobalConfirmDialogFragment(tag))
    }

    override fun getDialogResult() =
        controller.currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            tag,
            false
        )

    override fun setDialogResult(result: Boolean) {
        controller.previousBackStackEntry?.savedStateHandle?.set(tag, result)
    }
}
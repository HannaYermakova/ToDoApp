package by.aermakova.todoapp.ui.dialog.selectItem.keyResult

import androidx.navigation.NavController
import by.aermakova.todoapp.AppNavigationDirections
import by.aermakova.todoapp.ui.dialog.selectItem.SelectItemDialogNavigation

private const val DIALOG_RESULT = "select_key_result_dialog_result"

class SelectKeyResultDialogNavigation(
    private val controller: NavController
) : SelectItemDialogNavigation(controller) {

    override fun getTag() = DIALOG_RESULT

    override fun openItemDialog(title: String) {
        controller.navigate(AppNavigationDirections.actionGlobalSelectKeyResultDialogFragment(title))
    }

    fun openItemDialog(title: String, goalId: Long) {
        controller.navigate(AppNavigationDirections.actionGlobalSelectKeyResultDialogFragment(title, goalId))
    }
}
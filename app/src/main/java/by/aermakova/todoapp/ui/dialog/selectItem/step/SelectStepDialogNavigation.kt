package by.aermakova.todoapp.ui.dialog.selectItem.step

import androidx.navigation.NavController
import by.aermakova.todoapp.AppNavigationDirections
import by.aermakova.todoapp.ui.dialog.selectItem.SelectItemDialogNavigation

private const val DIALOG_RESULT = "select_step_dialog_result"

class SelectStepDialogNavigation(
    private val controller: NavController
) : SelectItemDialogNavigation(controller) {

    override fun getTag() = DIALOG_RESULT

    override fun openItemDialog(title: String) {
        controller.navigate(AppNavigationDirections.actionGlobalSelectStepDialogFragment(title))
    }

    fun openItemDialog(title: String, goalId: Long) {
        controller.navigate(AppNavigationDirections.actionGlobalSelectStepDialogFragment(title, goalId))
    }
}
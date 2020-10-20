package by.aermakova.todoapp.ui.dialog.selectItem.goal

import androidx.navigation.NavController
import by.aermakova.todoapp.AppNavigationDirections
import by.aermakova.todoapp.ui.dialog.selectItem.SelectItemDialogNavigation

private const val DIALOG_RESULT = "select_goal_dialog_result"

class SelectGoalDialogNavigation(
    private val controller: NavController
) : SelectItemDialogNavigation(controller) {

    override fun getTag(): String = DIALOG_RESULT

    override fun openItemDialog(title: String) {
        controller.navigate(AppNavigationDirections.actionGlobalSelectItemDialog(title))
    }
}
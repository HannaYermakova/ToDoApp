package by.aermakova.todoapp.ui.dialog.selectItem.goal

import androidx.navigation.NavController
import by.aermakova.todoapp.AppNavigationDirections
import by.aermakova.todoapp.ui.dialog.selectItem.SelectItemDialogNavigation
import javax.inject.Inject

class SelectGoalDialogNavigation @Inject constructor(
    private val controller: NavController
) : SelectItemDialogNavigation(controller) {

    override fun openItemDialog(title: String) {
        controller.navigate(AppNavigationDirections.actionGlobalSelectItemDialog(title))
    }
}
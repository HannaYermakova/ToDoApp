package by.aermakova.todoapp.ui.dialog.selectItem

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import by.aermakova.todoapp.AppNavigationDirections
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import javax.inject.Inject

private const val DIALOG_RESULT = "dialog_result"

abstract class SelectItemDialogNavigation (private val controller: NavController) : DialogNavigation<Long> {

    override fun getDialogResult(): MutableLiveData<Long>? {
        return controller.currentBackStackEntry?.savedStateHandle?.getLiveData<Long>(DIALOG_RESULT)
    }

    override fun setDialogResult(id: Long) {
        controller.previousBackStackEntry?.savedStateHandle?.set(DIALOG_RESULT, id)
    }
}
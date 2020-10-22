package by.aermakova.todoapp.ui.dialog.convertIdea

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import by.aermakova.todoapp.AppNavigationDirections
import by.aermakova.todoapp.ui.main.MainFlowFragmentDirections
import by.aermakova.todoapp.ui.navigation.DialogNavigation


private const val DIALOG_RESULT = "convert_idea_dialog_result"

class ConvertIdeaDialogNavigator(private val controller: NavController) : DialogNavigation<Boolean> {

    override fun openItemDialog(title: String) {
       controller.navigate(AppNavigationDirections.actionGlobalConvertIdeaIntoTaskDialogFragment())
    }

    fun openConvertIdeaDialog(ideaId: Long) {
       controller.navigate(AppNavigationDirections.actionGlobalConvertIdeaIntoTaskDialogFragment(ideaId))
    }

    override fun getDialogResult(): MutableLiveData<Boolean>? {
        return controller.currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(DIALOG_RESULT)
    }

    override fun setDialogResult(result: Boolean) {
        controller.previousBackStackEntry?.savedStateHandle?.set(DIALOG_RESULT, result)
    }
}
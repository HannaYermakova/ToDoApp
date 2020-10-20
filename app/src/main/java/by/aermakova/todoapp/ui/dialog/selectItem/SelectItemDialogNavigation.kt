package by.aermakova.todoapp.ui.dialog.selectItem

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import by.aermakova.todoapp.ui.navigation.DialogNavigation


abstract class SelectItemDialogNavigation (private val controller: NavController) : DialogNavigation<Long> {

    private val dialogResult : String
        get()  = getTag()

    abstract fun getTag(): String

    override fun getDialogResult(): MutableLiveData<Long>? {
        Log.d("A_SelectItemDialog", "getDialogResult")
        return controller.currentBackStackEntry?.savedStateHandle?.getLiveData<Long>(dialogResult)
    }

    override fun setDialogResult(id: Long) {
        Log.d("A_SelectItemDialog", "setDialogResult $id")
        controller.previousBackStackEntry?.savedStateHandle?.set(dialogResult, id)
    }
}
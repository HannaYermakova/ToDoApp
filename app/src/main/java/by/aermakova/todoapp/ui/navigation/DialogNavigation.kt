package by.aermakova.todoapp.ui.navigation

import androidx.lifecycle.MutableLiveData

interface DialogNavigation {

    fun openAddItemDialog(title: String)

    fun getDialogResult(): MutableLiveData<String>?

    fun setDialogResult(text:String)
}

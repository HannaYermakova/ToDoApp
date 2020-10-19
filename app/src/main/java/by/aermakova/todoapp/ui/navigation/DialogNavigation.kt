package by.aermakova.todoapp.ui.navigation

import androidx.lifecycle.MutableLiveData

interface DialogNavigation<Type> {

    fun openAddItemDialog(title: String)

    fun getDialogResult(): MutableLiveData<Type>?

    fun setDialogResult(text:Type)
}

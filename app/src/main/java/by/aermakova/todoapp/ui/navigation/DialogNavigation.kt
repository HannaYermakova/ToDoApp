package by.aermakova.todoapp.ui.navigation

import androidx.lifecycle.MutableLiveData

interface DialogNavigation<Type> {

    fun openItemDialog(title: String)

    fun getDialogResult(): MutableLiveData<Type>?

    fun setDialogResult(result:Type)
}

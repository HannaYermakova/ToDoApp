package by.aermakova.todoapp.ui.base

import androidx.lifecycle.LiveData
import by.aermakova.todoapp.data.model.FunctionNoArgs

interface DetailsScreen{
    val openEditFragment: FunctionNoArgs
    val editButtonIsVisible: LiveData<Boolean>
}
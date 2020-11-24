package by.aermakova.todoapp.ui.base

import by.aermakova.todoapp.data.model.FunctionNoArgs

interface DetailsScreen{
    val openEditFragment: FunctionNoArgs
    val editButtonIsVisible: Boolean
}
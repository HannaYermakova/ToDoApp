package by.aermakova.todoapp.ui.idea.main

import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import javax.inject.Inject

class IdeaViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation
) : BaseViewModel() {

    val addNewElement = { mainFlowNavigation.navigateToAddNewElementFragment() }
}
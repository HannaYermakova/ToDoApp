package by.aermakova.todoapp.ui.navigation

interface MainFlowNavigation {

    fun navigateToAddNewElementFragment()

    fun navigateToShowDetailsFragment(id: Long)

    fun popBack()
}
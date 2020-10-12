package by.aermakova.todoapp.ui.navigation

interface FragmentNavigation {

    fun addNewElementFragment()

    fun showDetailsFragment(id: Long)

    fun popBack()
}
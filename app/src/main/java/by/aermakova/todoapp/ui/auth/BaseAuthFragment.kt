package by.aermakova.todoapp.ui.auth

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import by.aermakova.todoapp.data.remote.auth.LoginAuthorizationListener
import by.aermakova.todoapp.ui.base.BaseFragment
import javax.inject.Inject

abstract class BaseAuthFragment<VM : ViewModel, Binding : ViewDataBinding> :
    BaseFragment<VM, Binding>() {

    @Inject
    lateinit var authListener: LoginAuthorizationListener

    override fun onStart() {
        super.onStart()
        authListener.registerListener()
    }

    override fun onStop() {
        super.onStop()
        authListener.unregisterListener()
    }
}
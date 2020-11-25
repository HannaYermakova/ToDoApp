package by.aermakova.todoapp.ui.auth

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import by.aermakova.todoapp.data.remote.auth.LoginAuthorizationListener
import by.aermakova.todoapp.ui.base.BaseFragment
import by.aermakova.todoapp.util.hideKeyboard
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseAuthFragment<VM : BaseAuthViewModel, Binding : ViewDataBinding> :
    BaseFragment<VM, Binding>() {

    @Inject
    lateinit var authListener: LoginAuthorizationListener

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        compositeDisposable.add(
            viewModel.fragmentState.subscribe({
                requireActivity().hideKeyboard()
                viewModel.setStatus(it)
            },
                { it.printStackTrace() })
        )
    }

    override fun onStart() {
        super.onStart()
        authListener.registerListener()
    }

    override fun onStop() {
        super.onStop()
        authListener.unregisterListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
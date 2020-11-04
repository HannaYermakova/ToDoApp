package by.aermakova.todoapp.ui.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import by.aermakova.todoapp.BR
import by.aermakova.todoapp.util.hideKeyboard
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseFragment<VM : ViewModel, Binding : ViewDataBinding> :
    Fragment(), HasSupportFragmentInjector {

    @get:LayoutRes
    protected abstract val layout: Int

    protected lateinit var binding: Binding

    @Inject
    protected lateinit var viewModel: VM

    open var bindingViewModelId: Int = BR.viewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layout, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidSupportInjection.inject(this)
        bindViewModel(binding, viewModel)
    }

    override fun onPause() {
        requireActivity().hideKeyboard()
        super.onPause()
    }

    override fun onResume() {
        requireActivity().hideKeyboard()
        super.onResume()
    }

    private fun bindViewModel(binding: Binding, viewModel: VM) {
        binding.setVariable(bindingViewModelId, viewModel)
    }

    private var fragmentInjector: DispatchingAndroidInjector<Fragment>? = null

    @Inject
    fun injectDependencies(fragmentInjector: DispatchingAndroidInjector<Fragment>) {
        this.fragmentInjector = fragmentInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentInjector
    }
}
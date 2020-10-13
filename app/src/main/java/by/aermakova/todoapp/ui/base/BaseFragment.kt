package by.aermakova.todoapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

private const val DEFAULT_BINDING_VIEW_MODEL_ID = -1

abstract class BaseFragment<VM : ViewModel, Binding : ViewDataBinding> :
    Fragment(), HasSupportFragmentInjector {

    @get:LayoutRes
    protected abstract val layout: Int

    protected lateinit var binding: Binding

    @Inject
    protected lateinit var viewModel: VM

    open var bindingViewModelId: Int = DEFAULT_BINDING_VIEW_MODEL_ID

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

    private fun bindViewModel(binding: Binding, viewModel: VM) {
        if (bindingViewModelId > DEFAULT_BINDING_VIEW_MODEL_ID) {
            binding.setVariable(bindingViewModelId, viewModel)
        }
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
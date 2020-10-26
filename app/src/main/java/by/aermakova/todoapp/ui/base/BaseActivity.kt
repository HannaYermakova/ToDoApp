package by.aermakova.todoapp.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseActivity<VM: ViewModel>: AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    protected lateinit var viewModel: VM

    private var fragmentInjector: DispatchingAndroidInjector<Fragment>? = null

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? = fragmentInjector

    @Inject
    fun injectDependencies(fragmentInjector: DispatchingAndroidInjector<Fragment>) {
        this.fragmentInjector = fragmentInjector
    }

}
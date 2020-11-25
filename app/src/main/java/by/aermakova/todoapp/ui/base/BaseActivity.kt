package by.aermakova.todoapp.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import by.aermakova.todoapp.data.remote.auth.LoginAuthorizationListener
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseActivity<VM: ViewModel>: AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var authListener: LoginAuthorizationListener

    @Inject
    protected lateinit var viewModel: VM

    private var fragmentInjector: DispatchingAndroidInjector<Fragment>? = null

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? = fragmentInjector

    @Inject
    fun injectDependencies(fragmentInjector: DispatchingAndroidInjector<Fragment>) {
        this.fragmentInjector = fragmentInjector
    }


    override fun onStart() {
        super.onStart()
        authListener.registerListener()
    }

    override fun onStop() {
        super.onStop()
        authListener.unregisterListener()
    }

}
package by.aermakova.todoapp.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentLoginBinding
import by.aermakova.todoapp.ui.base.BaseFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class LoginFragment : BaseFragment<FragmentLoginBinding>(), HasSupportFragmentInjector {

    @Inject
    lateinit var viewModel: LoginViewModel

    private var fragmentInjector: DispatchingAndroidInjector<Fragment>? = null

    @Inject
    fun injectDependencies(fragmentInjector: DispatchingAndroidInjector<Fragment>) {
        this.fragmentInjector = fragmentInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentInjector
    }

    @Inject
    lateinit var facebookLoginHelper: FacebookLoginHelper

    override val layout: Int
        get() = R.layout.fragment_login

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidSupportInjection.inject(this)
        facebookLoginHelper.setFacebookListener(binding.loginButtonFacebook, this)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        facebookLoginHelper.fbCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
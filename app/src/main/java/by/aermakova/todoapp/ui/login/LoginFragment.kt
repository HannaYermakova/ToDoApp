package by.aermakova.todoapp.ui.login

import android.content.Intent
import android.os.Bundle
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentLoginBinding
import by.aermakova.todoapp.ui.base.BaseFragment
import javax.inject.Inject

class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    @Inject
    lateinit var facebookLoginHelper: FacebookLoginHelper

    override val layout: Int
        get() = R.layout.fragment_login

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
package by.aermakova.todoapp.ui.auth.login

import android.content.Intent
import android.os.Bundle
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.remote.auth.loginManager.FacebookLoginManager
import by.aermakova.todoapp.databinding.FragmentLoginBinding
import by.aermakova.todoapp.ui.auth.BaseAuthFragment
import javax.inject.Inject

class LoginFragment : BaseAuthFragment<LoginViewModel, FragmentLoginBinding>() {

    @Inject
    lateinit var facebookLoginManager: FacebookLoginManager

    override val layout: Int
        get() = R.layout.fragment_login

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.facebookLoginManager = facebookLoginManager
        compositeDisposable.add(
            viewModel.facebookLoginManagerListener.subscribe(
                { facebookLoginManager.signInWithFacebookAccount(this) },
                { it.printStackTrace() })
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        facebookLoginManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
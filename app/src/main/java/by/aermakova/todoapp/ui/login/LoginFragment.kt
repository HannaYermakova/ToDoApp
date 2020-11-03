package by.aermakova.todoapp.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.remote.auth.loginManager.FacebookLoginManager
import by.aermakova.todoapp.databinding.FragmentLoginBinding
import by.aermakova.todoapp.util.Status
import by.aermakova.todoapp.util.hideKeyboard
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LoginFragment : BaseLoginFragment<LoginViewModel, FragmentLoginBinding>() {

    @Inject
    lateinit var facebookLoginManager: FacebookLoginManager

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    override val layout: Int
        get() = R.layout.fragment_login

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.fragmentState.observe(viewLifecycleOwner, Observer {
            requireActivity().hideKeyboard()
            viewModel.setStatus(it)
        })

        binding.loginButtonFacebook.setOnClickListener {
            viewModel.setState(Status.LOADING)
            facebookLoginManager.signInWithFacebookAccount(this)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        facebookLoginManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
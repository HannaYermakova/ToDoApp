package by.aermakova.todoapp.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentLoginBinding
import by.aermakova.todoapp.ui.base.BaseFragment
import com.facebook.CallbackManager

private const val FACEBOOK_PERMISSION_EMAIL = "email"
private const val FACEBOOK_PERMISSION_PUBLIC_PROFILE = "public_profile"

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var hostController: NavController
    private val fbCallbackManager = CallbackManager.Factory.create()

    override val layout: Int
        get() = R.layout.fragment_login

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            hostController = Navigation.findNavController(it, R.id.app_host_fragment)
            setFacebookListener()
        }
    }

    private fun setFacebookListener() {
        with(binding.loginButtonFacebook) {
            setReadPermissions(FACEBOOK_PERMISSION_EMAIL, FACEBOOK_PERMISSION_PUBLIC_PROFILE)
            fragment = this@LoginFragment
        }
        viewModel.registerFacebookLoginListener(fbCallbackManager, hostController)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        fbCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
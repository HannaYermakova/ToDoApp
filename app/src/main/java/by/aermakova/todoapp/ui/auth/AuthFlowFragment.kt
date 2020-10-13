package by.aermakova.todoapp.ui.auth

import android.os.Bundle
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentAuthFlowBinding
import by.aermakova.todoapp.ui.base.BaseFragment
import by.aermakova.todoapp.ui.navigation.AuthFlowNavigation
import javax.inject.Inject

class AuthFlowFragment : BaseFragment<AuthViewModel, FragmentAuthFlowBinding>() {

    @Inject
    lateinit var authFlowNavigationSettings : AuthFlowNavigation.Settings

    override val layout: Int
        get() = R.layout.fragment_auth_flow

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        authFlowNavigationSettings.attachNavigationControllerToView(binding.root)
    }
}
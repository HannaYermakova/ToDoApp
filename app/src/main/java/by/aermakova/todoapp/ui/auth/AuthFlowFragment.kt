package by.aermakova.todoapp.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentAuthFlowBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class AuthFlowFragment : BaseFragment<FragmentAuthFlowBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    override val layout: Int
        get() = R.layout.fragment_auth_flow

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Navigation.setViewNavController(binding.root, Navigation.findNavController(binding.root))
    }
}
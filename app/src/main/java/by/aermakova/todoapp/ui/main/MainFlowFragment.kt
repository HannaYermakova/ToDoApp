package by.aermakova.todoapp.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentMainFlowBinding
import by.aermakova.todoapp.ui.base.BaseFragment
import kotlin.concurrent.fixedRateTimer

class MainFlowFragment : BaseFragment<FragmentMainFlowBinding>() {

    private val viewModel: MainFlowViewModel by viewModels()
    private lateinit var controller: NavController

    override val layout: Int
        get() = R.layout.fragment_main_flow

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = Navigation.findNavController(view.findViewById(R.id.main_host_fragment))
        Navigation.setViewNavController(binding.root, controller)
        NavigationUI.setupWithNavController(binding.bottomNavigationView, controller)
    }
}
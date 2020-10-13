package by.aermakova.todoapp.ui.main

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentMainFlowBinding
import by.aermakova.todoapp.ui.base.BaseFragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class MainFlowFragment : BaseFragment<MainFlowViewModel, FragmentMainFlowBinding>() {

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
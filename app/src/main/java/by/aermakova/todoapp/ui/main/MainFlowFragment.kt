package by.aermakova.todoapp.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentMainFlowBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class MainFlowFragment : BaseFragment<FragmentMainFlowBinding>() {

    private val viewModel: MainFlowViewModel by viewModels()

    override val layout: Int
        get() = R.layout.fragment_main_flow

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Navigation.setViewNavController(binding.root, NavHostFragment.findNavController(this))
    }
}
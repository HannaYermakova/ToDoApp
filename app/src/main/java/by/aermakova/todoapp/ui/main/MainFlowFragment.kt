package by.aermakova.todoapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentMainFlowBinding
import by.aermakova.todoapp.ui.base.BaseFragment
import com.facebook.AccessToken

class MainFlowFragment : BaseFragment<FragmentMainFlowBinding>() {

    private val viewModel: MainFlowViewModel by viewModels()
    private lateinit var controller: NavController

    override val layout: Int
        get() = R.layout.fragment_main_flow

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = Navigation.findNavController(binding.root)
        Navigation.setViewNavController(binding.root, controller)
    }
}
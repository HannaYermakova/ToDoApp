package by.aermakova.todoapp.ui.main

import android.os.Bundle
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentMainFlowBinding
import by.aermakova.todoapp.ui.base.BaseFragment
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import javax.inject.Inject

class MainFlowFragment : BaseFragment<MainFlowViewModel, FragmentMainFlowBinding>() {

    @Inject
    lateinit var navigationSetting: MainFlowNavigation.Settings

    override val layout: Int
        get() = R.layout.fragment_main_flow

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navigationSetting.attachNavigationControllerToNavView(binding.bottomNavigationView)
    }
}
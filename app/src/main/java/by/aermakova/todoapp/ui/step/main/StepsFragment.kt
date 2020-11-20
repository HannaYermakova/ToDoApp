package by.aermakova.todoapp.ui.step.main

import android.os.Bundle
import androidx.lifecycle.Observer
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentStepsBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class StepsFragment : BaseFragment<StepsViewModel, FragmentStepsBinding>() {

    override val layout: Int
        get() = R.layout.fragment_steps

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.confirmDeleteListener?.observe(viewLifecycleOwner, Observer {
            viewModel.confirmDelete(it)
        })
    }
}
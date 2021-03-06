package by.aermakova.todoapp.ui.goal.main

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentGoalsBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class GoalsFragment : BaseFragment<GoalsViewModel, FragmentGoalsBinding>() {

    override val layout: Int
        get() = R.layout.fragment_goals

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.logoutObserver?.observe(viewLifecycleOwner, Observer {
            if (it) viewModel.exit()
        })

        viewModel.keyResultObserver?.observe(
            viewLifecycleOwner,
            Observer {
                viewModel.addKeyResultToSelectedGoal(it)
            })

        viewModel.confirmDeleteListener?.observe(viewLifecycleOwner, Observer {
            viewModel.confirmDelete(it)
        })
    }
}
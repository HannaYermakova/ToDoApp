package by.aermakova.todoapp.ui.goal

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentAddGoalBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class AddGoalFragment : BaseFragment<AddGoalViewModel, FragmentAddGoalBinding>() {

    override val layout: Int
        get() = R.layout.fragment_add_goal

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.keyResultObserver?.observe(viewLifecycleOwner, Observer {
            viewModel.addTempKeyResult(it)
        })
    }
}
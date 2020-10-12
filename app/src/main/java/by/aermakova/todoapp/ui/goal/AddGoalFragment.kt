package by.aermakova.todoapp.ui.goal

import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentAddGoalBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class AddGoalFragment : BaseFragment<FragmentAddGoalBinding>() {

    private lateinit var viewModel: AddGoalViewModel

    override val layout: Int
        get() = R.layout.fragment_add_goal



}
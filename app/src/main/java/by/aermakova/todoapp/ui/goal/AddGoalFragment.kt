package by.aermakova.todoapp.ui.goal

import by.aermakova.todoapp.BR
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentAddGoalBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class AddGoalFragment : BaseFragment<AddGoalViewModel, FragmentAddGoalBinding>() {

    override var bindingViewModelId = BR.viewModel

    override val layout: Int
        get() = R.layout.fragment_add_goal
}
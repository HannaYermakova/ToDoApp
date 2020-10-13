package by.aermakova.todoapp.ui.goal

import by.aermakova.todoapp.BR
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentGoalsBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class GoalsFragment : BaseFragment<GoalsViewModel, FragmentGoalsBinding>() {

    override var bindingViewModelId = BR.viewModel

    override val layout: Int
        get() = R.layout.fragment_goals
}
package by.aermakova.todoapp.ui.goal.details

import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentGoalDetailsBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class GoalDetailsFragment : BaseFragment<GoalDetailsViewModel, FragmentGoalDetailsBinding>() {

    override val layout: Int
        get() = R.layout.fragment_goal_details
}
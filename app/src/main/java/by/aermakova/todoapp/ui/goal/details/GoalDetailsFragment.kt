package by.aermakova.todoapp.ui.goal.details

import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentGoalDetailsBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class GoalDetailsFragment : BaseFragment<GoalDetailsViewModel, FragmentGoalDetailsBinding>() {

    val args: GoalDetailsFragmentArgs by navArgs()

    override val layout: Int
        get() = R.layout.fragment_goal_details
}
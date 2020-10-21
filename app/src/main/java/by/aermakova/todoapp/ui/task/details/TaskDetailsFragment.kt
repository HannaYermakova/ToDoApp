package by.aermakova.todoapp.ui.task.details

import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentTaskDetailsBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class TaskDetailsFragment : BaseFragment<TaskDetailsViewModel, FragmentTaskDetailsBinding>() {

    override val layout: Int
        get() = R.layout.fragment_task_details

}
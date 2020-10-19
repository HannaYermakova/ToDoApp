package by.aermakova.todoapp.ui.task

import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentTasksBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class TasksFragment : BaseFragment<TasksViewModel, FragmentTasksBinding>() {

    override val layout: Int
        get() = R.layout.fragment_tasks
}
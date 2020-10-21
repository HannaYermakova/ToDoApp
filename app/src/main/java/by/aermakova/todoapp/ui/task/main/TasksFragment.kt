package by.aermakova.todoapp.ui.task.main

import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentTasksBinding
import by.aermakova.todoapp.ui.base.BaseFragment
import by.aermakova.todoapp.ui.task.main.TasksViewModel

class TasksFragment : BaseFragment<TasksViewModel, FragmentTasksBinding>() {

    override val layout: Int
        get() = R.layout.fragment_tasks
}
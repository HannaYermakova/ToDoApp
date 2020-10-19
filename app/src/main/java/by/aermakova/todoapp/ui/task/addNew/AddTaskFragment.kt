package by.aermakova.todoapp.ui.task.addNew

import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentAddItemBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class AddTaskFragment : BaseFragment<AddTaskViewModel, FragmentAddItemBinding>() {

    override val layout: Int
        get() = R.layout.fragment_add_task
}
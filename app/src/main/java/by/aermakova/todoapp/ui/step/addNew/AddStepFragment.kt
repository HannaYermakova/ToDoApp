package by.aermakova.todoapp.ui.step.addNew

import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentAddStepBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class AddStepFragment : BaseFragment<AddStepViewModel, FragmentAddStepBinding>() {

    override val layout: Int
        get() = R.layout.fragment_add_step
}
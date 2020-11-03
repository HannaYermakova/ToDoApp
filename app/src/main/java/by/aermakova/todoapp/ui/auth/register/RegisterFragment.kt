package by.aermakova.todoapp.ui.auth.register

import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentRegisterBinding
import by.aermakova.todoapp.ui.auth.BaseAuthFragment

class RegisterFragment : BaseAuthFragment<RegisterViewModel, FragmentRegisterBinding>() {

    override val layout: Int
        get() = R.layout.fragment_register
}
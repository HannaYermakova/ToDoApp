package by.aermakova.todoapp.ui.register

import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentRegisterBinding
import by.aermakova.todoapp.ui.login.BaseLoginFragment

class RegisterFragment : BaseLoginFragment<RegisterViewModel, FragmentRegisterBinding>() {

    override val layout: Int
        get() = R.layout.fragment_register
}
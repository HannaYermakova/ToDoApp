package by.aermakova.todoapp.ui.register

import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.data.remote.auth.loginManager.EmailLoginManager
import io.reactivex.Observer
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val emailLoginManager: EmailLoginManager
) : BaseViewModel() {

    val registerWithEmailButton = { registerWithEmail() }

    private val _emailText = BehaviorSubject.create<String>()
    val emailText: Observer<String>
        get() = _emailText

    private val _passwordText = BehaviorSubject.create<String>()
    val passwordText: Observer<String>
        get() = _passwordText

    private fun registerWithEmail() {
        if (_emailText.value != null && _passwordText.value != null) {
            val email = _emailText.value!!
            val password = _passwordText.value!!
            emailLoginManager.createAccount(email, password)
        }
    }
}
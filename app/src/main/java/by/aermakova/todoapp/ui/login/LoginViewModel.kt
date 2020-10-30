package by.aermakova.todoapp.ui.login

import android.util.Log
import by.aermakova.todoapp.data.remote.auth.FirebaseAuthUtil
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.util.Status
import by.aermakova.todoapp.data.remote.auth.loginManager.EmailLoginManager
import io.reactivex.Observer
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val emailLoginManager: EmailLoginManager,
    private val loginNavigation: LoginNavigation
) : BaseViewModel() {

    init {
        if (!FirebaseAuthUtil.isSingIn()) {
            Log.d("A_LoginViewModel", "LoginViewModel init")
            _status.onNext(Status.SUCCESS)
        }
    }

    val loginWithEmailButton = { loginWithEmail() }

    val registerWithEmailButton = { registerWithEmail() }

    private fun registerWithEmail() {
        loginNavigation.navigateToRegisterFragment()
    }

    private val _emailText = BehaviorSubject.create<String>()
    val emailText: Observer<String>
        get() = _emailText

    private val _passwordText = BehaviorSubject.create<String>()
    val passwordText: Observer<String>
        get() = _passwordText

    private fun loginWithEmail() {
        if (_emailText.value != null && _passwordText.value != null) {
            val email = _emailText.value!!
            val password = _passwordText.value!!
            emailLoginManager.signInWithEmailAndPassword(email, password)
        }
    }
}
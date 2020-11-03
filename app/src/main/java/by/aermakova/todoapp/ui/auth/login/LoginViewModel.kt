package by.aermakova.todoapp.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.remote.auth.loginManager.EmailLoginManager
import by.aermakova.todoapp.ui.auth.LoginNavigation
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.util.Status
import io.reactivex.Observer
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val emailLoginManager: EmailLoginManager,
    private val loginNavigation: LoginNavigation
) : BaseViewModel() {

    val loginWithEmailButton = { loginWithEmail() }

    val registerWithEmailButton = { registerWithEmail() }

    private val _fragmentState = MutableLiveData<Status>()
    val fragmentState: LiveData<Status>
        get() = _fragmentState

    fun setStatus(status: Status) {
        _status.onNext(status)
    }

    fun setState(status: Status) {
        _fragmentState.postValue(status)
    }

    private fun registerWithEmail() {
        loginNavigation.navigateToRegisterFragment()
    }

    private val _emailText = BehaviorSubject.create<String>()
    val emailText: Observer<String>
        get() = _emailText

    private val _passwordText = BehaviorSubject.create<String>()
    val passwordText: Observer<String>
        get() = _passwordText

    init {

    }

    private fun loginWithEmail() {
        if (!_emailText.value.isNullOrEmpty() && !_passwordText.value.isNullOrEmpty()) {
            val email = _emailText.value!!.trim()
            val password = _passwordText.value!!.trim()
            setState(Status.LOADING)
            emailLoginManager.signInWithEmailAndPassword(email, password)
        }
    }
}
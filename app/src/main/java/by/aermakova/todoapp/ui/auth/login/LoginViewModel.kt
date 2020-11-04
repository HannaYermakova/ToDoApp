package by.aermakova.todoapp.ui.auth.login

import by.aermakova.todoapp.data.remote.auth.loginManager.AppLoginManager
import by.aermakova.todoapp.data.remote.auth.loginManager.EmailLoginManager
import by.aermakova.todoapp.data.remote.auth.loginManager.FacebookLoginManager
import by.aermakova.todoapp.ui.auth.LoginNavigation
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.util.Status
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    val emailLoginManager: EmailLoginManager,
    private val loginNavigation: LoginNavigation,
    statusListener: Subject<Status>
) : BaseViewModel() {

    var facebookLoginManager: FacebookLoginManager? = null

    val loginButton: (AppLoginManager) -> Unit = { loginUser(it) }

    val registerWithEmailButton = { registerWithEmail() }

    private val _facebookLoginManagerListener = BehaviorSubject.create<Boolean>()
    val facebookLoginManagerListener: Observable<Boolean>
        get() = _facebookLoginManagerListener

    private val _fragmentState = BehaviorSubject.create<Status>()
    val fragmentState: Observable<Status>
        get() = _fragmentState

    fun setStatus(status: Status) {
        _status.onNext(status)
    }

    private fun setState(status: Status) {
        _fragmentState.onNext(status)
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
        disposable.add(
            statusListener.subscribe(
                { setState(it) },
                { it.printStackTrace() }
            )
        )
    }

    private fun loginUser(manager: AppLoginManager) {
        setState(Status.LOADING)
        when (manager) {
            is EmailLoginManager -> loginWithEmail(manager.errorMessage)
            is FacebookLoginManager -> loginWithFacebook()
        }
    }

    private fun loginWithEmail(errorMessage: String?) {
        if (!_emailText.value.isNullOrEmpty() && !_passwordText.value.isNullOrEmpty()) {
            val email = _emailText.value!!.trim()
            val password = _passwordText.value!!.trim()
            setState(Status.LOADING)
            emailLoginManager.signInWithEmailAndPassword(email, password)
        } else setState(Status.ERROR.apply { message = errorMessage ?: "" })
    }

    private fun loginWithFacebook() {
        _facebookLoginManagerListener.onNext(true)
    }
}
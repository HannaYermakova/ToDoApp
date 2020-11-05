package by.aermakova.todoapp.ui.auth.login

import by.aermakova.todoapp.data.remote.auth.EmailCredentials
import by.aermakova.todoapp.data.remote.auth.loginManager.AppLoginManager
import by.aermakova.todoapp.data.remote.auth.loginManager.EmailLoginManager
import by.aermakova.todoapp.data.remote.auth.loginManager.FacebookLoginManager
import by.aermakova.todoapp.ui.auth.BaseAuthViewModel
import by.aermakova.todoapp.ui.auth.LoginNavigation
import by.aermakova.todoapp.util.Status
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    val emailLoginManager: EmailLoginManager,
    private val loginNavigation: LoginNavigation,
    private val statusListener: Subject<Status>
) : BaseAuthViewModel() {

    override val stateListener: Subject<Status>
        get() = statusListener

    var facebookLoginManager: FacebookLoginManager? = null

    val loginButton: (AppLoginManager) -> Unit = { loginUser(it) }

    val registerWithEmailButton = { registerWithEmail() }

    private val _facebookLoginManagerListener = BehaviorSubject.create<Boolean>()

    val facebookLoginManagerListener: Observable<Boolean>
        get() = _facebookLoginManagerListener

    private fun registerWithEmail() {
        loginNavigation.navigateToRegisterFragment()
    }

    private fun loginUser(manager: AppLoginManager) {
        setState(Status.LOADING)
        when (manager) {
            is EmailLoginManager -> enterWithEmail(emailLoginManager.errorMessage)
            is FacebookLoginManager -> loginWithFacebook()
        }
    }

    init {
        startListenStatusChange()
    }

    override fun enterWithEmailCredentials(it: EmailCredentials) {
        emailLoginManager.signInWithEmailAndPassword(it)
    }

    private fun loginWithFacebook() {
        _facebookLoginManagerListener.onNext(true)
    }
}
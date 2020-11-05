package by.aermakova.todoapp.ui.auth.register

import by.aermakova.todoapp.data.remote.auth.EmailCredentials
import by.aermakova.todoapp.data.remote.auth.loginManager.EmailLoginManager
import by.aermakova.todoapp.ui.auth.BaseAuthViewModel
import by.aermakova.todoapp.util.Status
import io.reactivex.subjects.Subject
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val emailLoginManager: EmailLoginManager,
    private val statusListener: Subject<Status>
) : BaseAuthViewModel() {

    val registerWithEmailButton = { enterWithEmail(emailLoginManager.errorMessage) }

    override val stateListener: Subject<Status>
        get() = statusListener

    init {
        startListenStatusChange()
    }

    override fun enterWithEmailCredentials(it: EmailCredentials) {
        emailLoginManager.createAccount(it)
    }
}
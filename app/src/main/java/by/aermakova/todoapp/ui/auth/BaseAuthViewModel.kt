package by.aermakova.todoapp.ui.auth

import by.aermakova.todoapp.data.remote.auth.EmailCredentials
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.util.Status
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

abstract class BaseAuthViewModel : BaseViewModel() {

    abstract val stateListener: Subject<Status>

    private val _fragmentState = BehaviorSubject.create<Status>()
    val fragmentState: Observable<Status>
        get() = _fragmentState

    fun setStatus(status: Status) {
        _status.onNext(status)
    }

    fun setState(status: Status) {
        _fragmentState.onNext(status)
    }

    private val _emailText = BehaviorSubject.create<String>()
    val emailText: Observer<String>
        get() = _emailText

    private val _passwordText = BehaviorSubject.create<String>()
    val passwordText: Observer<String>
        get() = _passwordText

    protected fun enterWithEmail(errorMessage: String?) {
        setState(Status.LOADING)
        checkEmailAndPassword()?.let {
            enterWithEmailCredentials(it)
        } ?: setState(Status.ERROR.apply { message = errorMessage ?: "" })
    }

    abstract fun enterWithEmailCredentials(it: EmailCredentials)

    private fun checkEmailAndPassword(): EmailCredentials? {
        return if (_emailText.value != null && _passwordText.value != null) {
            EmailCredentials(_emailText.value!!, _passwordText.value!!)
        } else null
    }

    protected fun startListenStatusChange() {
        disposable.add(
            stateListener.subscribe(
                { setState(it) },
                { it.printStackTrace() }
            )
        )
    }
}
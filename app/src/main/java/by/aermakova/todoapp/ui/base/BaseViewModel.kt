package by.aermakova.todoapp.ui.base

import androidx.lifecycle.ViewModel
import by.aermakova.todoapp.util.Status
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

abstract class BaseViewModel : ViewModel() {

    val errorAction: (String) -> Unit = { _status.onNext(Status.ERROR.apply { message = it }) }

    val loadingAction: () -> Unit = { _status.onNext(Status.LOADING) }

    val successAction: () -> Unit = { _status.onNext(Status.SUCCESS) }

    private val _disposable = CompositeDisposable()
    val disposable: CompositeDisposable
        get() = _disposable

    override fun onCleared() {
        _disposable.clear()
        super.onCleared()
    }

    protected val _status = BehaviorSubject.create<Status>()
    val status: Observable<Status>
        get() = _status.hide()
}
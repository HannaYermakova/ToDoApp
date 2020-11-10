package by.aermakova.todoapp.ui.base

import androidx.lifecycle.ViewModel
import by.aermakova.todoapp.util.Status
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

abstract class BaseViewModel : ViewModel() {

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
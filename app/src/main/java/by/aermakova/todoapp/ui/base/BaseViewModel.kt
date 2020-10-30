package by.aermakova.todoapp.ui.base

import androidx.lifecycle.ViewModel
import by.aermakova.todoapp.util.Status
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject

abstract class BaseViewModel : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()
    val disposable: CompositeDisposable
        get() = compositeDisposable

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    protected val _status = ReplaySubject.create<Status>()
    val status: Observable<Status>
        get() = _status.hide()
}
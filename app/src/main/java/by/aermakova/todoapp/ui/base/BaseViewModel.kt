package by.aermakova.todoapp.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected val _disposable = CompositeDisposable()
    val disposable: CompositeDisposable
        get() = _disposable

    override fun onCleared() {
        _disposable.clear()
        super.onCleared()
    }
}
package by.aermakova.todoapp.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()
    val disposable: CompositeDisposable
        get() = compositeDisposable

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
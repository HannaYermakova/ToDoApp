package by.aermakova.todoapp.ui.base

import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

abstract class BaseDialogVieModel : BaseViewModel() {

    override val mainFlowNavigation: MainFlowNavigation?
        get() = null

    protected val _dismissCommand = BehaviorSubject.create<Boolean>()
    val dismissCommand: Observable<Boolean>
        get() = _dismissCommand.hide()

    val cancel = {
        doOnCancel()
        _dismissCommand.onNext(true)
    }

    val ok = {
        _dismissCommand.onNext(true)
        doOnOk()
    }

    abstract fun doOnCancel()
    abstract fun doOnOk()
}
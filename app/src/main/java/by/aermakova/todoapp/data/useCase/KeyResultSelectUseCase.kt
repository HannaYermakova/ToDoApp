package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.ui.adapter.TextModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class KeyResultSelectUseCase(
    private val goalInteractor: GoalInteractor
) {

    private val _keyResultsList = BehaviorSubject.create<List<TextModel>>()
    val keyResultsList: Observable<List<TextModel>>
        get() = _keyResultsList

    fun setKeyResultList(disposable: CompositeDisposable, goalId: Long) {
        disposable.add(
            goalInteractor.createKeyResultsList(goalId, _keyResultsList)
        )
    }
}
package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.model.TextModel
import by.aermakova.todoapp.data.model.toTextModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class KeyResultSelectUseCase(
    private val goalInteractor: GoalInteractor
) {

    private val _keyResultsList = BehaviorSubject.create<List<TextModel>>()
    val keyResultsList: Observable<List<TextModel>>
        get() = _keyResultsList

    private val _selectedKeyResult = BehaviorSubject.create<TextModel>()
    val selectedKeyResult: Observable<TextModel>
        get() = _selectedKeyResult

    fun setKeyResultList(disposable: CompositeDisposable, goalId: Long) {
        disposable.add(
            goalInteractor.createKeyResultsList(goalId, _keyResultsList)
        )
    }

    fun addSelectedKeyResult(
        disposable: CompositeDisposable,
        keyResultId: Long
    ) {
        disposable.add(
            goalInteractor.getKeyResultsById(keyResultId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {_selectedKeyResult.onNext(it.toTextModel()) },
                    { it.printStackTrace() }
                )
        )
    }
}
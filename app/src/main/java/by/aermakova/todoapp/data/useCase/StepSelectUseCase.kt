package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.model.TextModel
import by.aermakova.todoapp.data.model.toTextModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class StepSelectUseCase(
    private val stepInteractor: StepInteractor
) {

    private val _stepsList = BehaviorSubject.create<List<TextModel>>()
    val stepsList: Observable<List<TextModel>>
        get() = _stepsList

    fun setStepsList(keyResultId: Long, disposable: CompositeDisposable) {
        disposable.add(stepInteractor.getStepsByKeyResultId(keyResultId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { entity -> entity.map { it.toTextModel() } }
            .subscribe(
                { _stepsList.onNext(it) },
                { it.printStackTrace() }
            ))
    }
}
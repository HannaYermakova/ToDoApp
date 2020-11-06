package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.ui.adapter.TextModel
import by.aermakova.todoapp.ui.adapter.toTextModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class GoalSelectUseCase(
    private val goalInteractor: GoalInteractor
) {

    private val _goalsList = BehaviorSubject.create<List<TextModel>>()
    val goalsList: Observable<List<TextModel>>
        get() = _goalsList

    fun addCreationOfGoalListToDisposable(disposable: CompositeDisposable) {
        disposable.add(
            goalInteractor.getAllUndoneGoals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _goalsList.onNext(it.map { item -> item.toTextModel() }) },
                    { it.printStackTrace() }
                )
        )
    }
}
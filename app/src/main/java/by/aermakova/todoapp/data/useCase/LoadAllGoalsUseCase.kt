package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.interactor.GoalInteractor
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoadAllGoalsUseCase(
    private val goalInteractor: GoalInteractor,
    private val errorMessage: String
) {

    fun saveUpdatedGoal(
        goalId: Long,
        disposable: CompositeDisposable,
        mapAction: () -> Disposable?,
        successAction: () -> Unit,
        errorAction: ((String) -> Unit)?

    ) {
        disposable.add(
            Single.create<Boolean> {
                it.onSuccess(goalInteractor.updateGoal(true, goalId))
            }
                .map { mapAction.invoke() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { successAction.invoke() },
                    {
                        errorAction?.invoke(errorMessage)
                        it.printStackTrace()
                    }
                )
        )
    }
}
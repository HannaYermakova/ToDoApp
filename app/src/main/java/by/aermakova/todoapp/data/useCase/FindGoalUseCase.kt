package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.db.entity.GoalEntity
import by.aermakova.todoapp.data.db.entity.GoalKeyResults
import by.aermakova.todoapp.data.db.entity.KeyResultEntity
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.model.FunctionString
import by.aermakova.todoapp.util.handleError
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FindGoalUseCase(
    private val goalInteractor: GoalInteractor,
    private val errorMessage: String
) {

    fun getGoalKeyResultsById(
        goalId: Long, disposable: CompositeDisposable,
        errorAction: FunctionString,
        successAction: (GoalKeyResults) -> Unit
    ) {
        disposable.add(goalInteractor.getGoalKeyResultsById(goalId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { successAction.invoke(it) },
                { it.handleError(errorMessage, errorAction) }
            ))
    }

    fun useGoalById(
        goalId: Long?,
        successAction: (GoalEntity) -> Unit,
        errorAction: FunctionString? = null
    ): Disposable? = goalId?.let {
        goalInteractor.getGoalById(goalId).observeEntity(
            successAction, errorAction
        )
    }

    fun useGoalById(
        disposable: CompositeDisposable,
        goalId: Long?,
        successAction: (GoalEntity) -> Unit,
        errorAction: FunctionString? = null
    ) {
        goalId?.let {
            disposable.add(
                goalInteractor.getGoalById(goalId).observeEntity(
                    successAction, errorAction
                )
            )
        }
    }

    fun useKeyResultById(
        keyResultId: Long?,
        successAction: (KeyResultEntity) -> Unit,
        errorAction: ((String?) -> Unit)? = null
    ): Disposable? = keyResultId?.let {
        goalInteractor.getKeyResultsById(keyResultId).observeEntity(
            successAction, errorAction
        )
    }
}

fun <Entity> Observable<Entity>.observeEntity(
    successAction: (Entity) -> Unit,
    errorAction: FunctionString? = null
): Disposable {
    return observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { successAction.invoke(it) },
            { it.handleError(it.message, errorAction) }
        )
}

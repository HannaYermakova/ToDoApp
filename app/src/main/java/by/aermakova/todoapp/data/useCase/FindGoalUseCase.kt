package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.db.entity.GoalEntity
import by.aermakova.todoapp.data.db.entity.KeyResultEntity
import by.aermakova.todoapp.data.interactor.GoalInteractor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class FindGoalUseCase(
    private val goalInteractor: GoalInteractor
) {

    fun useGoalById(
        goalId: Long?,
        successAction: (GoalEntity) -> Unit,
        errorAction: ((String?) -> Unit)? = null
    ): Disposable? = goalId?.let {
        goalInteractor.getGoalById(goalId).observeEntity(
            successAction, errorAction
        )
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
    errorAction: ((String?) -> Unit)? = null
): Disposable {
    return observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { successAction.invoke(it) },
            {
                it.printStackTrace()
                errorAction?.invoke(it.message)
            }
        )
}

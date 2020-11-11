package by.aermakova.todoapp.data.useCase

import android.util.Log
import androidx.lifecycle.LiveData
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.ui.dialog.addItem.AddItemDialogNavigation
import by.aermakova.todoapp.ui.goal.main.INIT_SELECTED_GOAL_ID
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AddKeyResultToGoalUseCase(
    private val goalInteractor: GoalInteractor,
    private val dialogNavigation: AddItemDialogNavigation,
    private val addKeyResultDialogTitle: String
) {

    private var goalId = INIT_SELECTED_GOAL_ID

    val keyResultObserver: LiveData<String>?
        get() = dialogNavigation.getDialogResult()

    fun openDialog(goalId: Long) {
        this.goalId = goalId
        dialogNavigation.openItemDialog(addKeyResultDialogTitle)
    }

    fun addKeyResult(
        keyResultTitle: String,
        disposable: CompositeDisposable
    ) {
        val id = goalId
        Log.d("A_AddKeyResultToGoal", "$goalId")
        Log.d("A_AddKeyResultToGoal", "$id")
        disposable.add(Single.create<Long> {
            it.onSuccess(goalInteractor.addKeyResultToGoalInLocal(id, keyResultTitle))
        }.map { goalInteractor.getKeyResultsById(it) }
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    it.subscribe { keyResultEntity ->
                        goalInteractor.addKeyResultToGoalInRemote(
                            keyResultEntity
                        )
                        goalId = INIT_SELECTED_GOAL_ID
                    }
                },
                { it.printStackTrace() }
            )
        )
    }
}
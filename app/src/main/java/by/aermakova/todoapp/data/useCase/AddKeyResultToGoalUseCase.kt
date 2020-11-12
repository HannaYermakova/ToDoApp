package by.aermakova.todoapp.data.useCase

import androidx.lifecycle.LiveData
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.ui.dialog.addItem.AddItemDialogNavigation
import by.aermakova.todoapp.ui.goal.main.INIT_SELECTED_GOAL_ID
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class AddKeyResultToGoalUseCase(
    private val goalInteractor: GoalInteractor,
    private val dialogNavigation: AddItemDialogNavigation,
    private val addKeyResultDialogTitle: String,
    private val errorMessage: String
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
        disposable: CompositeDisposable,
        errorAction: (String) -> Unit
    ) {
        val command = BehaviorSubject.create<Boolean>()
        disposable.add(
            command.observeOn(AndroidSchedulers.mainThread()).subscribe(
                { if (it) errorAction.invoke(errorMessage) },
                { it.printStackTrace() }
            )
        )
        disposable.add(
            goalInteractor.getGoalById(goalId)
                .subscribeOn(Schedulers.io())
                .filter {
                    if (it.goalStatusDone) {
                        command.onNext(true)
                    }
                    !it.goalStatusDone
                }

                .map {
                    goalInteractor.addKeyResultToGoalInLocal(goalId, keyResultTitle)
                }
                .flatMap { goalInteractor.getKeyResultsById(it) }
                .subscribe(
                    {
                        goalInteractor.addKeyResultToGoalInRemote(it)
                        goalId = INIT_SELECTED_GOAL_ID
                    },
                    { it.printStackTrace() }
                )
        )
    }
}
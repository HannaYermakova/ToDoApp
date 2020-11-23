package by.aermakova.todoapp.data.useCase

import androidx.lifecycle.LiveData
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.model.FunctionString
import by.aermakova.todoapp.ui.dialog.addItem.AddItemDialogNavigation
import by.aermakova.todoapp.util.handleError
import io.reactivex.Observer
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

    val keyResultObserver: LiveData<String>?
        get() = dialogNavigation.getDialogResult()

    fun openDialog() {
        dialogNavigation.openItemDialog(addKeyResultDialogTitle)
    }

    fun addKeyResult(
        goalId: Long,
        keyResultTitle: String,
        disposable: CompositeDisposable,
        saveSuccess: Observer<Boolean>? = null,
        errorAction: FunctionString
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
                        saveSuccess?.onNext(true)
                    },
                    { it.handleError(errorMessage, errorAction) }
                )
        )
    }
}
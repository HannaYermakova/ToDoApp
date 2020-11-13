package by.aermakova.todoapp.data.useCase

import androidx.lifecycle.LiveData
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.model.toCommonModelStringList
import by.aermakova.todoapp.ui.dialog.addItem.AddItemDialogNavigation
import by.aermakova.todoapp.ui.goal.main.INIT_SELECTED_GOAL_ID
import by.aermakova.todoapp.util.handleError
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class AddNewKeyResultsToGoalUseCase(
    private val dialogNavigation: AddItemDialogNavigation,
    private val addKeyResultDialogTitle: String,
    private val errorMessage: String,
    private val addKeyResultToGoalUseCase: AddKeyResultToGoalUseCase
) {

    private var goalId = INIT_SELECTED_GOAL_ID

    val addKeyResult: (String) -> Unit =
        { dialogNavigation.openItemDialog(addKeyResultDialogTitle) }

    val keyResultObserver: LiveData<String>?
        get() = dialogNavigation.getDialogResult()

    private val _tempKeyResult = BehaviorSubject.create<List<String>>()

    private val _allKeyResultsList = BehaviorSubject.create<List<CommonModel>>()
    val allKeyResultsList: Observable<List<CommonModel>>
        get() = _allKeyResultsList.hide()

    private val newKeyResults = arrayListOf<String>()

    private val allKeyResults = arrayListOf<String>()

    fun initKeyResultList(
        goalId: Long,
        existingKeyResults: List<String>,
        disposable: CompositeDisposable,
        errorAction: (String) -> Unit
    ) {
        this.goalId = goalId
        allKeyResults.addAll(existingKeyResults)
        disposable.add(
            _tempKeyResult
                .subscribe(
                    { _allKeyResultsList.onNext(it.toCommonModelStringList()) },
                    { it.handleError(errorMessage, errorAction) }
                )
        )
    }

    fun addTempKeyResult(newKeyResult: String) {
        newKeyResults.add(newKeyResult)
        allKeyResults.add(newKeyResult)
        _tempKeyResult.onNext(allKeyResults)
    }

    fun saveChanges(disposable: CompositeDisposable, errorAction: (String) -> Unit) {
        newKeyResults.forEach {
            addKeyResultToGoalUseCase.addKeyResult(goalId, it, disposable, errorAction)
        }
    }
}
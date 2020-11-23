package by.aermakova.todoapp.data.useCase

import androidx.lifecycle.LiveData
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.model.FunctionString
import by.aermakova.todoapp.data.model.toCommonModelStringList
import by.aermakova.todoapp.ui.dialog.addItem.AddItemDialogNavigation
import by.aermakova.todoapp.ui.goal.main.INIT_SELECTED_ITEM_ID
import by.aermakova.todoapp.util.handleError
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class AddNewKeyResultsToGoalUseCase(
    private val dialogNavigation: AddItemDialogNavigation,
    private val addKeyResultDialogTitle: String,
    private val errorMessage: String,
    private val addKeyResultToGoalUseCase: AddKeyResultToGoalUseCase
) {

    private var goalId = INIT_SELECTED_ITEM_ID

    val addKeyResult: FunctionString =
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
        errorAction: FunctionString
    ) {
        this.goalId = goalId
        allKeyResults.addAll(existingKeyResults)
        _allKeyResultsList.onNext(allKeyResults.toCommonModelStringList())
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

    fun saveChanges(
        disposable: CompositeDisposable,
        saveSuccess: Observer<Boolean>,
        errorAction: FunctionString
    ) {
        newKeyResults.forEach {
            addKeyResultToGoalUseCase.addKeyResult(goalId, it, disposable, saveSuccess, errorAction)
        }
        if (newKeyResults.isEmpty()) {
            saveSuccess.onNext(false)
        }
    }
}
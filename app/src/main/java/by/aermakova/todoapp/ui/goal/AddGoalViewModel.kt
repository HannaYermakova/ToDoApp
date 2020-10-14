package by.aermakova.todoapp.ui.goal

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import by.aermakova.todoapp.ui.adapter.Model
import by.aermakova.todoapp.ui.adapter.toModel
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class AddGoalViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    private val dialogNavigation: DialogNavigation
) : ViewModel() {

    val popBack = { mainFlowNavigation.popBack() }

    val addKeyResult: (String) -> Unit = {
        dialogNavigation.openAddItemDialog(it)
    }

    val saveGoal = { }

    val keyResultObserver: LiveData<String>?
        get() = dialogNavigation.getDialogResult()

    private val _tempKeyResultsList = BehaviorSubject.create<List<Model<String>>>()
    val tempKeyResultsList: Observable<List<Model<String>>>
        get() = _tempKeyResultsList.hide()

    private val _tempKeyResult = BehaviorSubject.create<List<String>>()

    private val _disposable = CompositeDisposable()
    val disposable: CompositeDisposable
        get() = _disposable

    private val tempKeyResults = arrayListOf<String>()

    fun addTempKeyResult(tempKeyResult: String) {
        tempKeyResults.add(tempKeyResult)
        _tempKeyResult.onNext(tempKeyResults)
    }

    init {
        _disposable.add(
            _tempKeyResult
                .subscribe({ it ->
                    var i = 0
                    _tempKeyResultsList.onNext(it.map { toModel(i++, it) })
                },
                    { it.printStackTrace() })
        )
    }

    override fun onCleared() {
        _disposable.clear()
        super.onCleared()
    }
}
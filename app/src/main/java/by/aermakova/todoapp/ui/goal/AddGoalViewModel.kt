package by.aermakova.todoapp.ui.goal

import androidx.lifecycle.LiveData
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.ui.adapter.ModelWrapper
import by.aermakova.todoapp.ui.adapter.toModelStringList
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class AddGoalViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    private val dialogNavigation: DialogNavigation,
    private val goalInteractor: GoalInteractor
) : BaseViewModel() {

    val popBack = { mainFlowNavigation.popBack() }

    val addKeyResult: (String) -> Unit = {
        dialogNavigation.openAddItemDialog(it)
    }

    val saveGoal = { saveGoalToLocalDataBase() }

    private fun saveGoalToLocalDataBase() {
        disposable.add(
            Single.create<Boolean> {
                if (!_tempGoalTitle.value.isNullOrBlank()) {
                    goalInteractor.saveGoal(_tempGoalTitle.value!!, tempKeyResults)
                    it.onSuccess(true)
                } else {
                    it.onSuccess(false)
                }
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it) mainFlowNavigation.popBack()
                },
                    { it.printStackTrace() })
        )
    }

    val keyResultObserver: LiveData<String>?
        get() = dialogNavigation.getDialogResult()

    private val _tempKeyResultsList = BehaviorSubject.create<List<ModelWrapper<String>>>()
    val tempKeyResultsList: Observable<List<ModelWrapper<String>>>
        get() = _tempKeyResultsList.hide()

    private val _tempGoalTitle = BehaviorSubject.create<String>()
    val tempGoalTitle: Observer<String>
        get() = _tempGoalTitle

    private val _tempKeyResult = BehaviorSubject.create<List<String>>()

    private val tempKeyResults = arrayListOf<String>()

    fun addTempKeyResult(tempKeyResult: String) {
        tempKeyResults.add(tempKeyResult)
        _tempKeyResult.onNext(tempKeyResults)
    }

    init {
        compositeDisposable.add(
            _tempKeyResult
                .subscribe({
                    _tempKeyResultsList.onNext(it.toModelStringList())
                },
                    { it.printStackTrace() })
        )
    }
}
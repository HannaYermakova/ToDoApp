package by.aermakova.todoapp.ui.goal

import androidx.lifecycle.LiveData
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.ui.adapter.CommonModel
import by.aermakova.todoapp.ui.adapter.toCommonModelStringList
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
    private val dialogNavigation: DialogNavigation<String>,
    private val goalInteractor: GoalInteractor
) : BaseViewModel() {

    val popBack = { mainFlowNavigation.popBack() }

    val addKeyResult: (String) -> Unit = {
        dialogNavigation.openItemDialog(it)
    }

    val saveGoal = { saveGoalToLocalDataBaseAndSyncToRemote() }

    private fun saveGoalToLocalDataBaseAndSyncToRemote() {
        if (!_tempGoalTitle.value.isNullOrBlank()) {
            disposable.add(
                Single.create<Long> {
                    it.onSuccess(
                        goalInteractor.saveGoalAndKeyResToLocal(
                            _tempGoalTitle.value!!,
                            tempKeyResults
                        )
                    )
                }
                    .map {
                        goalInteractor.getGoalKeyResultsById(it).subscribe { goalKeyResults ->
                            goalInteractor.saveGoalAndKeyResultsToRemote(goalKeyResults)
                        }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { mainFlowNavigation.popBack() },
                        { it.printStackTrace() }
                    )
            )
        }
    }

    val keyResultObserver: LiveData<String>?
        get() = dialogNavigation.getDialogResult()

    private val _tempKeyResultsList = BehaviorSubject.create<List<CommonModel>>()
    val tempKeyResultsList: Observable<List<CommonModel>>
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
                .subscribe(
                    { _tempKeyResultsList.onNext(it.toCommonModelStringList()) },
                    { it.printStackTrace() })
        )
    }
}
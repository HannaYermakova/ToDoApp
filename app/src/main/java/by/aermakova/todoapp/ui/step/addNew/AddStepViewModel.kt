package by.aermakova.todoapp.ui.step.addNew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.ui.adapter.TextModel
import by.aermakova.todoapp.ui.adapter.toTextModel
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class AddStepViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    private val goalInteractor: GoalInteractor,
    private val stepInteractor: StepInteractor
) : BaseViewModel() {

    val popBack = { mainFlowNavigation.popBack() }

    private val _tempStepTitle = BehaviorSubject.create<String>()
    val tempStepTitle: Observer<String>
        get() = _tempStepTitle

    private val _goalsList = BehaviorSubject.create<List<TextModel>>()
    val goalsList: Observable<List<TextModel>>
        get() = _goalsList

    private val _keyResultsList = BehaviorSubject.create<List<TextModel>>()
    val keyResultsList: Observable<List<TextModel>>
        get() = _keyResultsList

    private val _keyResultIsVisible = MutableLiveData<Boolean>(false)
    val keyResultIsVisible: LiveData<Boolean>
        get() = _keyResultIsVisible

    private val _goalTitle = MutableLiveData<String>()
    val goalTitle: LiveData<String>
        get() = _goalTitle

    private var tempGoalId: Long? = null

    private var tempKeyResultId: Long? = null

    val goalSelected: (Long) -> Unit = {
        addTempGoal(it)
    }

    val keyResultSelected: (Long) -> Unit = {
        addTempKeyResult(it)
    }

    val saveStep = { saveStepToLocalDataBaseAndSyncToRemote() }

    init {
        disposable.add(
            goalInteractor.getAllGoals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _goalsList.onNext(it.map { item -> item.toTextModel() }) },
                    { it.printStackTrace() }
                )
        )
    }

    private fun setKeyResultList(goalId: Long) {
        disposable.add(
            goalInteractor.getGoalKeyResultsById(goalId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    it.keyResults.map { entity ->
                        entity.toTextModel()
                    }
                }
                .subscribe(
                    { _keyResultsList.onNext(it) },
                    { it.printStackTrace() }
                )
        )
    }

    private fun addTempGoal(goalId: Long?) {
        goalId?.let {
            tempGoalId = goalId
            _keyResultIsVisible.postValue(tempGoalId != null)
            setKeyResultList(goalId)
        }
    }

    private fun addTempKeyResult(keyResultId: Long?) {
        keyResultId?.let {
            tempKeyResultId = keyResultId
        }
    }

    private fun saveStepToLocalDataBaseAndSyncToRemote() {
        if (!_tempStepTitle.value.isNullOrBlank()
            && tempGoalId != null
            && tempKeyResultId != null
        ) {
            disposable.add(
                stepInteractor.createStep(
                    _tempStepTitle.value!!,
                    tempGoalId!!,
                    tempKeyResultId!!
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { mainFlowNavigation.popBack() },
                        { it.printStackTrace() }
                    )
            )
        }
    }
}
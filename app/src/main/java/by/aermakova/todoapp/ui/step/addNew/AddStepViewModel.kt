package by.aermakova.todoapp.ui.step.addNew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.db.entity.toCommonModel
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.dialog.selectItem.goal.SelectGoalDialogNavigation
import by.aermakova.todoapp.ui.dialog.selectItem.keyResult.SelectKeyResultDialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Named

class AddStepViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    @Named("SelectGoal") private val selectGoalDialogNavigation: SelectGoalDialogNavigation,
    @Named("SelectKeyResult") private val selectKeyResDialogNavigation: SelectKeyResultDialogNavigation,
    private val goalInteractor: GoalInteractor,
    private val stepInteractor: StepInteractor
) : BaseViewModel() {

    val popBack = { mainFlowNavigation.popBack() }

    private val _tempStepTitle = BehaviorSubject.create<String>()
    val tempStepTitle: Observer<String>
        get() = _tempStepTitle

    private val _keyResultIsVisible = MutableLiveData<Boolean>(false)
    val keyResultIsVisible: LiveData<Boolean>
        get() = _keyResultIsVisible

    private val _stepsIsVisible = MutableLiveData<Boolean>(false)
    val stepsIsVisible: LiveData<Boolean>
        get() = _stepsIsVisible

    private val _goalTitle = MutableLiveData<String>()
    val goalTitle: LiveData<String>
        get() = _goalTitle

    private val _keyResultTitle = MutableLiveData<String>()
    val keyResultTitle: LiveData<String>
        get() = _keyResultTitle

    val selectedGoalObserver: LiveData<Long>?
        get() = selectGoalDialogNavigation.getDialogResult()

    val selectedKeyResObserver: LiveData<Long>?
        get() = selectKeyResDialogNavigation.getDialogResult()

    private var tempGoalId: Long? = null

    private var tempKeyResultId: Long? = null

    val selectGoal: (String) -> Unit = { selectGoalDialogNavigation.openItemDialog(it) }

    val selectKeyResult: (String) -> Unit =
        { title -> tempGoalId?.let { selectKeyResDialogNavigation.openItemDialog(title, it) } }

    val saveStep = { saveStepToLocalDataBaseAndSyncToRemote() }

    fun addTempGoal(goalId: Long?) {
        goalId?.let {
            tempGoalId = goalId
            _keyResultIsVisible.postValue(tempGoalId != null)
            disposable.add(
                goalInteractor.getGoalKeyResultsById(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { goalKeyResults ->
                        _goalTitle.postValue(goalKeyResults.goal.text)
                    }
            )
        }
    }

    fun addTempKeyResult(keyResultId: Long?) {
        keyResultId?.let {
            tempKeyResultId = keyResultId
            _stepsIsVisible.postValue(tempKeyResultId != null)
            disposable.add(
                goalInteractor.getKeyResultsById(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { entity -> entity.toCommonModel() }
                    .subscribe { keyResultKeyResults ->
                        _keyResultTitle.postValue(keyResultKeyResults.text)
                    }
            )
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
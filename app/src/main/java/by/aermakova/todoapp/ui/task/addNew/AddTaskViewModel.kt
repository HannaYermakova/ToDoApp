package by.aermakova.todoapp.ui.task.addNew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.db.entity.toCommonModel
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.ui.adapter.toCommonModel
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.dialog.selectItem.goal.SelectGoalDialogNavigation
import by.aermakova.todoapp.ui.dialog.selectItem.keyResult.SelectKeyResultDialogNavigation
import by.aermakova.todoapp.ui.dialog.selectItem.step.SelectStepDialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Named

private const val INITIAL_VALUE = -1L

class AddTaskViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    @Named("SelectGoal") private val selectGoalDialogNavigation: SelectGoalDialogNavigation,
    @Named("SelectKeyResult") private val selectKeyResDialogNavigation: SelectKeyResultDialogNavigation,
    @Named("SelectStep") private val selectStepDialogNavigation: SelectStepDialogNavigation,
    private val goalInteractor: GoalInteractor,
    private val stepInteractor: StepInteractor
) : BaseViewModel() {

    val popBack = { mainFlowNavigation.popBack() }

    private val _tempTaskTitle = BehaviorSubject.create<String>()
    val tempTaskTitle: Observer<String>
        get() = _tempTaskTitle

    val saveTask = { saveTaskToLocalDataBaseAndSyncToRemote() }

    val selectGoal: (String) -> Unit = { selectGoalDialogNavigation.openItemDialog(it) }

    val selectKeyResult: (String) -> Unit =
        { selectKeyResDialogNavigation.openItemDialog(it, tempGoalId) }

    val selectStep: (String) -> Unit = {
        selectStepDialogNavigation.openItemDialog(it, tempKeyResultId)
    }

    private var tempGoalId = INITIAL_VALUE

    private var tempKeyResultId = INITIAL_VALUE

    private var tempStepId = INITIAL_VALUE

    val selectedGoalObserver: LiveData<Long>?
        get() = selectGoalDialogNavigation.getDialogResult()

    val selectedKeyResObserver: LiveData<Long>?
        get() = selectKeyResDialogNavigation.getDialogResult()

    val selectedStepObserver: LiveData<Long>?
        get() = selectStepDialogNavigation.getDialogResult()

    private val _keyResultIsVisible = MutableLiveData<Boolean>(false)
    val keyResultIsVisible: LiveData<Boolean>
        get() = _keyResultIsVisible

    private val _stepsIsVisible = MutableLiveData<Boolean>(false)
    val stepsIsVisible: LiveData<Boolean>
        get() = _stepsIsVisible

    private val _stepsIsSelected = MutableLiveData<Boolean>(false)
    val stepsIsSelected: LiveData<Boolean>
        get() = _stepsIsSelected

    private val _goalTitle = MutableLiveData<String>()
    val goalTitle: LiveData<String>
        get() = _goalTitle

    private val _keyResultTitle = MutableLiveData<String>()
    val keyResultTitle: LiveData<String>
        get() = _keyResultTitle

    private val _stepTitle = MutableLiveData<String>()
    val stepTitle: LiveData<String>
        get() = _stepTitle

    fun addTempGoal(goalId: Long?) {
        goalId?.let {
            tempGoalId = goalId
            _keyResultIsVisible.postValue(tempGoalId > INITIAL_VALUE)
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
            _stepsIsVisible.postValue(tempKeyResultId > INITIAL_VALUE)
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

    fun addTempStep(stepId: Long?) {
        stepId?.let {
            tempStepId = stepId
            _stepsIsSelected.postValue(tempStepId > INITIAL_VALUE)
            disposable.add(
                stepInteractor.getStepById(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { entity -> entity.toCommonModel {} }
                    .subscribe { step ->
                        _stepTitle.postValue(step.text)
                    }
            )
        }
    }

    private fun saveTaskToLocalDataBaseAndSyncToRemote() {
        mainFlowNavigation.popBack()
    }
}
package by.aermakova.todoapp.ui.task.addNew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.db.entity.Interval
import by.aermakova.todoapp.data.db.entity.toCommonModel
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.ui.adapter.toCommonModel
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.dialog.datePicker.PickDayDialogNavigator
import by.aermakova.todoapp.ui.dialog.selectItem.goal.SelectGoalDialogNavigation
import by.aermakova.todoapp.ui.dialog.selectItem.keyResult.SelectKeyResultDialogNavigation
import by.aermakova.todoapp.ui.dialog.selectItem.step.SelectStepDialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.convertLongToDate
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Named

class AddTaskViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    @Named("SelectGoal") private val selectGoalDialogNavigation: SelectGoalDialogNavigation,
    @Named("SelectKeyResult") private val selectKeyResDialogNavigation: SelectKeyResultDialogNavigation,
    @Named("SelectStep") private val selectStepDialogNavigation: SelectStepDialogNavigation,
    @Named("PickDate") private val pickDayDialogNavigation: PickDayDialogNavigator,
    private val goalInteractor: GoalInteractor,
    private val stepInteractor: StepInteractor,
    private val taskInteractor: TaskInteractor
) : BaseViewModel() {

    val popBack = { mainFlowNavigation.popBack() }

    private val _tempTaskTitle = BehaviorSubject.create<String>()
    val tempTaskTitle: Observer<String>
        get() = _tempTaskTitle

    val saveTask = { saveTaskToLocalDataBaseAndSyncToRemote() }

    val selectGoal: (String) -> Unit = { selectGoalDialogNavigation.openItemDialog(it) }

    val selectKeyResult: (String) -> Unit =
        { title -> tempGoalId?.let { selectKeyResDialogNavigation.openItemDialog(title, it) } }

    val selectStep: (String) -> Unit = { title ->
        tempKeyResultId?.let {
            selectStepDialogNavigation.openItemDialog(title, it)
        }
    }

    val pickFinishDay = { pickDayDialogNavigation.openItemDialog("") }

    private var tempGoalId: Long? = null

    private var tempKeyResultId: Long? = null

    private var tempStepId: Long? = null

    private var tempFinishTime: Long? = null

    val selectedGoalObserver: LiveData<Long>?
        get() = selectGoalDialogNavigation.getDialogResult()

    val selectedKeyResObserver: LiveData<Long>?
        get() = selectKeyResDialogNavigation.getDialogResult()

    val selectedStepObserver: LiveData<Long>?
        get() = selectStepDialogNavigation.getDialogResult()

    val selectedFinishTimeObserver: LiveData<Long>?
        get() = pickDayDialogNavigation.getDialogResult()

    private val _keyResultIsVisible = MutableLiveData<Boolean>(false)
    val keyResultIsVisible: LiveData<Boolean>
        get() = _keyResultIsVisible

    private val _stepsIsVisible = MutableLiveData<Boolean>(false)
    val stepsIsVisible: LiveData<Boolean>
        get() = _stepsIsVisible

    private val _stepsIsSelected = MutableLiveData<Boolean>(false)
    val stepsIsSelected: LiveData<Boolean>
        get() = _stepsIsSelected

    private val _finishDateIsSelected = MutableLiveData<Boolean>(false)
    val finishDateIsSelected: LiveData<Boolean>
        get() = _finishDateIsSelected

    private val _goalTitle = MutableLiveData<String>()
    val goalTitle: LiveData<String>
        get() = _goalTitle

    private val _keyResultTitle = MutableLiveData<String>()
    val keyResultTitle: LiveData<String>
        get() = _keyResultTitle

    private val _stepTitle = MutableLiveData<String>()
    val stepTitle: LiveData<String>
        get() = _stepTitle

    private val _finishDateText = MutableLiveData<String>()
    val finishDateText: LiveData<String>
        get() = _finishDateText

    val scheduledTask = MutableLiveData<Boolean>()

    val taskInterval = MutableLiveData<Interval>()

    val deadlinedTask = MutableLiveData<Boolean>(false)

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

    fun addTempStep(stepId: Long?) {
        stepId?.let {
            tempStepId = stepId
            _stepsIsSelected.postValue(tempStepId != null)
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

    fun checkAndSetFinishTime(finishTime: Long?) {
        if (finishTime != null && finishTime > System.currentTimeMillis()) {
            _finishDateIsSelected.postValue(true)
            _finishDateText.postValue(convertLongToDate(finishTime))
            tempFinishTime = finishTime
        }
    }

    private fun saveTaskToLocalDataBaseAndSyncToRemote() {
        if (!_tempTaskTitle.value.isNullOrBlank()
            && scheduledTask.value != null
        ) {
            disposable.add(
                Single.create<Long> {
                    it.onSuccess(
                        taskInteractor.saveTaskInLocalDatabase(
                            _tempTaskTitle.value!!,
                            tempGoalId,
                            tempKeyResultId,
                            tempStepId,
                            finishDate = if (deadlinedTask.value!!) {
                                tempFinishTime
                            } else null,
                            scheduledTask = scheduledTask.value!!,
                            interval = if (scheduledTask.value!!) taskInterval.value else null
                        )
                    )
                }
                    .map {
                        taskInteractor.getTaskById(it).subscribe { entity ->
                            taskInteractor.saveTaskToRemote(entity)
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
}
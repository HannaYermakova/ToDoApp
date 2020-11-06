package by.aermakova.todoapp.ui.task.addNew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.interactor.TaskCreator
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.ui.adapter.TextModel
import by.aermakova.todoapp.ui.adapter.toTextModel
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.dialog.datePicker.PickDayDialogNavigator
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.ITEM_IS_NOT_SELECTED_ID
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject


class AddTaskViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    private val pickDayDialogNavigation: PickDayDialogNavigator,
    private val goalInteractor: GoalInteractor,
    private val stepInteractor: StepInteractor,
    private val taskInteractor: TaskInteractor,
    private val errorMessage: String
) : BaseViewModel() {

    val popBack = { mainFlowNavigation.popBack() }

    private val saveAndClose = BehaviorSubject.create<Boolean>()

    private val _goalsList = BehaviorSubject.create<List<TextModel>>()
    val goalsList: Observable<List<TextModel>>
        get() = _goalsList

    private val _keyResultsList = BehaviorSubject.create<List<TextModel>>()
    val keyResultsList: Observable<List<TextModel>>
        get() = _keyResultsList

    private val _stepsList = BehaviorSubject.create<List<TextModel>>()
    val stepsList: Observable<List<TextModel>>
        get() = _stepsList

    private val _tempTaskTitle = BehaviorSubject.create<String>()
    val tempTaskTitle: Observer<String>
        get() = _tempTaskTitle

    val taskCreator = TaskCreator(
        pickDayDialogNavigation,
        taskInteractor,
        disposable,
        saveAndClose,
        _status,
        errorMessage
    )

    init {
        disposable.add(
            goalInteractor.createGoalsList(_goalsList)
        )

        disposable.add(
            _tempTaskTitle
                .subscribe { taskCreator.tempTaskTitle = it }
        )
        disposable.add(
            saveAndClose.subscribe { if (it) mainFlowNavigation.popBack() }
        )
    }

    val goalSelected: (Long) -> Unit = {
        addTempGoal(it)
    }

    val keyResultSelected: (Long) -> Unit = {
        addTempKeyResult(it)
    }

    val stepSelected: (Long) -> Unit = {
        addTempStep(it)
    }

    private val _keyResultIsVisible = MutableLiveData<Boolean>(false)
    val keyResultIsVisible: LiveData<Boolean>
        get() = _keyResultIsVisible

    private val _stepsIsVisible = MutableLiveData<Boolean>(false)
    val stepsIsVisible: LiveData<Boolean>
        get() = _stepsIsVisible

    private val _goalTitle = MutableLiveData<String>()
    val goalTitle: LiveData<String>
        get() = _goalTitle

    private fun addTempGoal(goalId: Long?) {
        goalId?.let {
            taskCreator.tempGoalId = if (it == ITEM_IS_NOT_SELECTED_ID) {
                null
            } else it
            _keyResultIsVisible.postValue(taskCreator.tempGoalId != null)
            taskCreator.tempGoalId?.let { id ->
                setKeyResultList(id)
            }
        }
    }

    private fun setKeyResultList(goalId: Long) {
        disposable.add(
            goalInteractor.createKeyResultsList(goalId, _keyResultsList)
        )
    }

    private fun addTempKeyResult(keyResultId: Long?) {
        keyResultId?.let {
            taskCreator.tempKeyResultId =
                if (it == ITEM_IS_NOT_SELECTED_ID) null
                else it
            _stepsIsVisible.postValue(
                taskCreator.tempKeyResultId != null
                        && taskCreator.tempGoalId != null
            )
            taskCreator.tempKeyResultId?.let { id ->
                setStepsList(id)
            }
        }
    }

    private fun setStepsList(keyResultId: Long) {
        disposable.add(
            stepInteractor.createStepsList(keyResultId, _stepsList)
        )
    }

    private fun addTempStep(stepId: Long?) {
        stepId?.let {
            taskCreator.tempStepId = if (stepId == ITEM_IS_NOT_SELECTED_ID) {
                null
            } else stepId
        }
    }
}
package by.aermakova.todoapp.ui.task.addNew

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.db.entity.StepEntity
import by.aermakova.todoapp.data.di.scope.NavigationTasks
import by.aermakova.todoapp.data.interactor.TaskCreator
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.data.useCase.FindStepUseCase
import by.aermakova.todoapp.data.useCase.GoalSelectUseCase
import by.aermakova.todoapp.data.useCase.KeyResultSelectUseCase
import by.aermakova.todoapp.data.useCase.StepSelectUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.dialog.datePicker.PickDayDialogNavigator
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.task.TasksNavigation
import by.aermakova.todoapp.util.ITEM_IS_NOT_SELECTED_ID
import by.aermakova.todoapp.util.Item
import io.reactivex.Observer
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject


class AddTaskViewModel @Inject constructor(
    @NavigationTasks private val navigation: TasksNavigation,
    private val findStepUseCase: FindStepUseCase,
    val goalSelectUseCase: GoalSelectUseCase,
    val keyResultSelectUseCase: KeyResultSelectUseCase,
    val stepSelectUseCase: StepSelectUseCase,
    taskInteractor: TaskInteractor,
    pickDayDialogNavigation: PickDayDialogNavigator,
    errorMessage: String,
    itemId: Long,
    code: Int?
) : BaseViewModel() {

    override val mainFlowNavigation: MainFlowNavigation?
        get() = navigation

    private val saveAndClose = BehaviorSubject.create<Boolean>()

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
                keyResultSelectUseCase.setKeyResultList(disposable, id)
            }
        }
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
                stepSelectUseCase.setStepsList(id, disposable)
            }
        }
    }

    private fun addTempStep(stepId: Long?) {
        stepId?.let {
            taskCreator.tempStepId = if (stepId == ITEM_IS_NOT_SELECTED_ID) {
                null
            } else stepId
        }
    }

    private val findStep: (StepEntity) -> Unit = {
        goalSelectUseCase.addCreationOfGoalListToDisposable(disposable, it.stepGoalId)
        keyResultSelectUseCase.addSelectedKeyResult(disposable, it.stepKeyResultId)
        stepSelectUseCase.addSelectedKeyResult(disposable, it.stepId)
    }

    init {
        code?.let {
            when (it) {
                Item.GOAL.code -> goalSelectUseCase.addCreationOfGoalListToDisposable(
                    disposable,
                    itemId
                )
                Item.STEP.code -> findStepUseCase.findStepById(itemId, findStep, errorAction)
                else -> Log.d("A_AddTaskViewModel", "Code of Item: $it")
            }
        }
        disposable.add(
            _tempTaskTitle
                .subscribe { taskCreator.tempTaskTitle = it }
        )
        disposable.add(
            saveAndClose.subscribe { if (it) popBack.invoke() }
        )
    }
}
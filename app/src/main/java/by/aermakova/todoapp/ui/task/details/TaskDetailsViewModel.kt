package by.aermakova.todoapp.ui.task.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.data.model.TaskModel
import by.aermakova.todoapp.data.model.toCommonModel
import by.aermakova.todoapp.data.useCase.FindGoalUseCase
import by.aermakova.todoapp.data.useCase.FindStepUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TaskDetailsViewModel @Inject constructor(
    private val navigation: MainFlowNavigation,
    private val taskInteractor: TaskInteractor,
    private val findGoal: FindGoalUseCase,
    private val findStep: FindStepUseCase,
    private val taskId: Long
) : BaseViewModel() {

    val popBack = { navigation.popBack() }

    val openEditFragment = { navigation.navigateToEditElementFragment(taskId) }

    private val _taskModel = MutableLiveData<TaskModel>()
    val taskModel: LiveData<TaskModel>
        get() = _taskModel

    private val _goalTitle = MutableLiveData<String>()
    val goalTitle: LiveData<String>
        get() = _goalTitle

    private val _keyResTitle = MutableLiveData<String>()
    val keyResTitle: LiveData<String>
        get() = _keyResTitle

    private val _stepTitle = MutableLiveData<String>()
    val stepTitle: LiveData<String>
        get() = _stepTitle

    val markAsDoneToggle = MutableLiveData<Boolean>(false)

    val markAsDone = { markTaskAsDone() }

    private fun markTaskAsDone() {
        val taskId = taskModel.value!!.taskId
        val status = markAsDoneToggle.value!!
        disposable.add(
            Single.create<Boolean> {
                taskInteractor.updateTask(status, taskId)
                it.onSuccess(true)
            }.map {
                taskInteractor
                    .getTaskById(taskId)
                    .subscribe({ entity ->
                        taskInteractor.updateTaskToRemote(entity)
                    },
                        { it.printStackTrace() })
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { navigation.popBack() },
                    { it.printStackTrace() }
                )
        )
    }

    init {
        disposable.add(
            taskInteractor
                .getTaskById(taskId)
                .map {
                    it.toCommonModel { }
                }
                .doOnSuccess { task ->
                    findGoal.useGoalById(task.goalId, { goal ->
                        _goalTitle.postValue(goal.text)
                    })
                }
                .doOnSuccess { task ->
                    findGoal.useKeyResultById(task.keyResultId, { keyRes ->
                        _keyResTitle.postValue(keyRes.text)
                    })
                }
                .doOnSuccess { task ->
                    findStep.useStepByIdInUiThread(task.stepId, {
                        _stepTitle.postValue(it.text)
                    }, errorAction)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _taskModel.postValue(it) },
                    { it.printStackTrace() }
                )
        )
    }
}
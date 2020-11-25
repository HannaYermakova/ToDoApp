package by.aermakova.todoapp.data.useCase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.db.entity.TaskEntity
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.data.model.FunctionString
import by.aermakova.todoapp.data.model.TaskModel
import by.aermakova.todoapp.data.model.toCommonModel
import by.aermakova.todoapp.util.handleError
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SetTaskFieldsUseCase(
    private val taskInteractor: TaskInteractor,
    private val findGoal: FindGoalUseCase,
    private val findStep: FindStepUseCase,
    private val errorMessage: String
) {

    private val _taskModel = MutableLiveData<TaskModel>()
    val taskModel: LiveData<TaskModel>
        get() = _taskModel

    private val _goalTitle = MutableLiveData<String>()
    val goalTitle: LiveData<String>
        get() = _goalTitle

    private val _goalIsVisible = MutableLiveData<Boolean>(false)
    val goalIsVisible: LiveData<Boolean>
        get() = _goalIsVisible

    private val _keyResTitle = MutableLiveData<String>()
    val keyResTitle: LiveData<String>
        get() = _keyResTitle

    private val _keyResIsVisible = MutableLiveData<Boolean>(false)
    val keyResIsVisible: LiveData<Boolean>
        get() = _keyResIsVisible

    private val _stepTitle = MutableLiveData<String>()
    val stepTitle: LiveData<String>
        get() = _stepTitle

    private val _stepIsVisible = MutableLiveData<Boolean>(false)
    val stepIsVisible: LiveData<Boolean>
        get() = _stepIsVisible

    fun setTaskFields(
        taskId: Long,
        disposable: CompositeDisposable,
        successAction: (TaskEntity) -> Unit,
        errorAction: FunctionString
    ) {
        disposable.add(
            taskInteractor
                .getTaskById(taskId)
                .map {
                    successAction.invoke(it)
                    it.toCommonModel { }
                }
                .doOnNext { task ->
                    findGoal.useGoalById(task.goalId, { goal ->
                        _goalIsVisible.postValue(true)
                        _goalTitle.postValue(goal.text)
                    })
                }
                .doOnNext { task ->
                    findGoal.useKeyResultById(task.keyResultId, { keyRes ->
                        _keyResIsVisible.postValue(true)
                        _keyResTitle.postValue(keyRes.text)
                    })
                }
                .doOnNext { task ->
                    findStep.useStepByIdInUiThread(task.stepId, {
                        _stepIsVisible.postValue(true)
                        _stepTitle.postValue(it.text)
                    }, errorAction)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _taskModel.postValue(it) },
                    { it.handleError(errorMessage, errorAction) }
                )
        )
    }
}
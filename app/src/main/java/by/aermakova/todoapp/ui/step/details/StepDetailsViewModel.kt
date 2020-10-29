package by.aermakova.todoapp.ui.step.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.ui.adapter.CommonModel
import by.aermakova.todoapp.ui.adapter.StepModel
import by.aermakova.todoapp.ui.adapter.toCommonModel
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Status
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class StepDetailsViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    private val goalInteractor: GoalInteractor,
    private val stepInteractor: StepInteractor,
    private val tasksInteractor: TaskInteractor,
    private val stepId: Long
) : BaseViewModel() {

    val popBack = { mainFlowNavigation.popBack() }

    private val _stepModel = MutableLiveData<StepModel>()
    val stepModel: LiveData<StepModel>
        get() = _stepModel

    private val _goalTitle = MutableLiveData<String>()
    val goalTitle: LiveData<String>
        get() = _goalTitle

    private val _keyResTitle = MutableLiveData<String>()
    val keyResTitle: LiveData<String>
        get() = _keyResTitle

    private val _stepTasks = MutableLiveData<List<CommonModel>>()
    val stepTasks: LiveData<List<CommonModel>>
        get() = _stepTasks

    val markAsDoneToggle = MutableLiveData<Boolean>(false)

    val markAsDone = { markStepAsDone() }

    private fun markStepAsDone() {
        val stepId = stepModel.value!!.stepId
        val status = markAsDoneToggle.value!!
        _status.onNext(Status.LOADING)
        disposable.add(
            Single.create<Boolean> {
                it.onSuccess(stepInteractor.updateStep(status, stepId))
            }
                .map { markTasksAsDone() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _status.onNext(Status.SUCCESS)
                        mainFlowNavigation.popBack()
                    },
                    {
                        _status.onNext(Status.ERROR)
                        it.printStackTrace()
                    }
                )
        )
    }

    private fun markTasksAsDone(): Disposable {
        return stepInteractor
            .getStepById(stepId)
            .subscribe(
                { entity ->
                    stepInteractor.updateStepToRemote(entity)
                    tasksInteractor.markStepsTasksAsDone(true, stepId)
                },
                { it.printStackTrace() }
            )
    }

    init {
        _status.onNext(Status.LOADING)
        disposable.add(
            stepInteractor
                .getStepById(stepId)
                .map { it.toCommonModel { } }
                .doOnSuccess { setGoalTitle(it) }
                .doOnSuccess { setKeyResultTitle(it) }
                .doOnSuccess { setTasksList(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _status.onNext(Status.SUCCESS)
                        _stepModel.postValue(it)
                    },
                    {
                        _status.onNext(Status.ERROR)
                        it.printStackTrace()
                    }
                )
        )
    }

    private fun setGoalTitle(step: StepModel): Disposable {
        return goalInteractor.getGoalById(step.goalId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { _goalTitle.postValue(it.text) },
                { error -> error.printStackTrace() }
            )
    }

    private fun setKeyResultTitle(step: StepModel): Disposable {
        return goalInteractor.getKeyResultsById(step.keyResultId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { keyRes -> _keyResTitle.postValue(keyRes.text) },
                { error -> error.printStackTrace() }
            )
    }

    private fun setTasksList(it: StepModel): Disposable {
        return tasksInteractor.getTaskByStepId(it.stepId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { tasks -> _stepTasks.postValue(tasks) },
                { error -> error.printStackTrace() }
            )
    }
}
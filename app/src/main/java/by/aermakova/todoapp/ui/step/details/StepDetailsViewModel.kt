package by.aermakova.todoapp.ui.step.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.ui.adapter.StepModel
import by.aermakova.todoapp.ui.adapter.toCommonModel
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class StepDetailsViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    private val goalInteractor: GoalInteractor,
    private val stepInteractor: StepInteractor,
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

    init {
        disposable.add(
            stepInteractor
                .getStepById(stepId)
                .map { it.toCommonModel { } }
                .doOnNext {
                    goalInteractor.getGoalById(it.goalId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { goal -> _goalTitle.postValue(goal.text) },
                            { error -> error.printStackTrace() }
                        )
                }
                .doOnNext {
                    goalInteractor.getKeyResultsById(it.keyResultId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { keyRes -> _keyResTitle.postValue(keyRes.text) },
                            { error -> error.printStackTrace() }
                        )
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _stepModel.postValue(it) },
                    { it.printStackTrace() }
                )
        )
    }
}
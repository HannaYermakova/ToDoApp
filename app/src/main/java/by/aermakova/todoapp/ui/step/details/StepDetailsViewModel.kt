package by.aermakova.todoapp.ui.step.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.di.scope.NavigationSteps
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.model.StepModel
import by.aermakova.todoapp.data.useCase.LoadStepUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import javax.inject.Inject

class StepDetailsViewModel @Inject constructor(
    @NavigationSteps private val navigation: MainFlowNavigation,
    private val loadStepUseCase: LoadStepUseCase,
    private val stepId: Long
) : BaseViewModel() {

    override val mainFlowNavigation: MainFlowNavigation
        get() = navigation

    val openEditFragment = { mainFlowNavigation.navigateToEditElementFragment(stepId) }

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

    private val _stepIdeas = MutableLiveData<List<CommonModel>>()
    val stepIdeas: LiveData<List<CommonModel>>
        get() = _stepIdeas

    val markAsDoneToggle = MutableLiveData<Boolean>(false)

    val markAsDone = { markStepAsDone() }

    private fun markStepAsDone() {
        val stepId = stepModel.value!!.stepId
        val status = markAsDoneToggle.value!!
        loadingAction.invoke()
        loadStepUseCase.markStepAsDone(
            disposable,
            stepId,
            status,
            {
                successAction.invoke()
                popBack.invoke()
            },
            errorAction
        )

    }

    init {
        loadingAction.invoke()
        loadStepUseCase.loadStep(
            disposable,
            { _goalTitle.postValue(it) },
            { _keyResTitle.postValue(it) },
            { _stepTasks.postValue(it) },
            { _stepIdeas.postValue(it) },
            {
                successAction.invoke()
                _stepModel.postValue(it)
            },
            errorAction
        )
    }
}
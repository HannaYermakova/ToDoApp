package by.aermakova.todoapp.ui.idea.addNew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.useCase.CreateIdeaUseCase
import by.aermakova.todoapp.data.useCase.GoalSelectUseCase
import by.aermakova.todoapp.data.useCase.KeyResultSelectUseCase
import by.aermakova.todoapp.data.useCase.StepSelectUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.ITEM_IS_NOT_SELECTED_ID
import by.aermakova.todoapp.util.Status
import io.reactivex.Observer
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject


class AddIdeaViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    private val createIdeaUseCase: CreateIdeaUseCase,
    val goalSelectUseCase: GoalSelectUseCase,
    val keyResultSelectUseCase: KeyResultSelectUseCase,
    val stepSelectUseCase: StepSelectUseCase
) : BaseViewModel() {

    val popBack = { mainFlowNavigation.popBack() }

    val saveIdea = { saveIdeaToLocalDataBaseAndSyncToRemote() }

    private val _tempIdeaTitle = BehaviorSubject.create<String>()
    val tempIdeaTitle: Observer<String>
        get() = _tempIdeaTitle

    private var tempGoalId: Long? = null

    private var tempKeyResultId: Long? = null

    private var tempStepId: Long? = null

    val goalSelected: (Long) -> Unit = {
        addTempGoal(it)
    }

    val keyResultSelected: (Long) -> Unit = {
        addTempKeyResult(it)
    }

    val stepSelected: (Long) -> Unit = {
        addTempStep(it)
    }

    init {
        goalSelectUseCase.addCreationOfGoalListToDisposable(disposable)
    }

    private val _keyResultIsVisible = MutableLiveData<Boolean>(false)
    val keyResultIsVisible: LiveData<Boolean>
        get() = _keyResultIsVisible

    private val _stepsIsVisible = MutableLiveData<Boolean>(false)
    val stepsIsVisible: LiveData<Boolean>
        get() = _stepsIsVisible

    private fun addTempGoal(goalId: Long?) {
        goalId?.let {
            tempGoalId = if (it == ITEM_IS_NOT_SELECTED_ID) {
                null
            } else it
            _keyResultIsVisible.postValue(tempGoalId != null)
            tempGoalId?.let { id ->
                keyResultSelectUseCase.setKeyResultList(disposable, id)
            }
        }
    }

    private fun addTempKeyResult(keyResultId: Long?) {
        keyResultId?.let {
            tempKeyResultId =
                if (it == ITEM_IS_NOT_SELECTED_ID) null
                else it
            _stepsIsVisible.postValue(
                tempKeyResultId != null
                        && tempGoalId != null
            )
            tempKeyResultId?.let { id ->
                stepSelectUseCase.setStepsList(id, disposable)
            }
        }
    }

    private fun addTempStep(stepId: Long?) {
        tempStepId = stepId
    }

    private fun saveIdeaToLocalDataBaseAndSyncToRemote() {
        _status.onNext(Status.LOADING)
        createIdeaUseCase.saveIdeaToLocalDataBaseAndSyncToRemote(
            disposable,
            _tempIdeaTitle.value,
            tempGoalId,
            tempKeyResultId,
            tempStepId,
            { mainFlowNavigation.popBack() },
            { _status.onNext(Status.ERROR.apply { message = it }) }
        )
    }
}
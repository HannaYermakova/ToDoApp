package by.aermakova.todoapp.ui.step.addNew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.useCase.GoalSelectUseCase
import by.aermakova.todoapp.data.useCase.KeyResultSelectUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.ITEM_IS_NOT_SELECTED_ID
import by.aermakova.todoapp.util.Status
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class AddStepViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    private val stepInteractor: StepInteractor,
    private val errorMessage: String,
    val goalSelectUseCase: GoalSelectUseCase,
    val keyResultSelectUseCase: KeyResultSelectUseCase
) : BaseViewModel() {

    val popBack = { mainFlowNavigation.popBack() }

    private val _tempStepTitle = BehaviorSubject.create<String>()
    val tempStepTitle: Observer<String>
        get() = _tempStepTitle

    private val _keyResultIsVisible = MutableLiveData<Boolean>(false)
    val keyResultIsVisible: LiveData<Boolean>
        get() = _keyResultIsVisible

    private val _goalTitle = MutableLiveData<String>()
    val goalTitle: LiveData<String>
        get() = _goalTitle

    private var tempGoalId: Long? = null

    private var tempKeyResultId: Long? = null

    val goalSelected: (Long) -> Unit = {
        addTempGoal(it)
    }

    val keyResultSelected: (Long) -> Unit = {
        addTempKeyResult(it)
    }

    val saveStep = { saveStepToLocalDataBaseAndSyncToRemote() }

    init {
        goalSelectUseCase.addCreationOfGoalListToDisposable(disposable)
    }

    private fun addTempGoal(goalId: Long?) {
        goalId?.let {
            tempGoalId = goalId
            _keyResultIsVisible.postValue(
                tempGoalId != null
                        && tempGoalId!! > ITEM_IS_NOT_SELECTED_ID
            )
            if (goalId > ITEM_IS_NOT_SELECTED_ID) {
                keyResultSelectUseCase.setKeyResultList(disposable, goalId)
            }
        }
    }

    private fun addTempKeyResult(keyResultId: Long?) {
        keyResultId?.let {
            tempKeyResultId = keyResultId
        }
    }

    private fun saveStepToLocalDataBaseAndSyncToRemote() {
        if (!_tempStepTitle.value.isNullOrBlank()
            && tempGoalId != null && tempGoalId!! > ITEM_IS_NOT_SELECTED_ID
            && tempKeyResultId != null && tempKeyResultId!! > ITEM_IS_NOT_SELECTED_ID
        ) {
            _status.onNext(Status.LOADING)
            disposable.add(
                stepInteractor.createStep(
                    _tempStepTitle.value!!,
                    tempGoalId!!,
                    tempKeyResultId!!
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { mainFlowNavigation.popBack() },
                        {
                            _status.onNext(Status.ERROR)
                            it.printStackTrace()
                        }
                    )
            )
        } else {
            _status.onNext(Status.ERROR.apply { message = errorMessage })
        }
    }
}
package by.aermakova.todoapp.ui.idea.addNew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.useCase.GoalSelectUseCase
import by.aermakova.todoapp.data.useCase.KeyResultSelectUseCase
import by.aermakova.todoapp.data.useCase.StepSelectUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.ITEM_IS_NOT_SELECTED_ID
import by.aermakova.todoapp.util.Status
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject


class AddIdeaViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    private val ideaInteractor: IdeaInteractor,
    private val errorMessage: String,
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

    init{
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
        if (!_tempIdeaTitle.value.isNullOrBlank()
            && tempGoalId != null && tempGoalId!! > ITEM_IS_NOT_SELECTED_ID
        ) {
            _status.onNext(Status.LOADING)
            disposable.add(
                Single.create<Long> {
                    it.onSuccess(
                        ideaInteractor
                            .saveIdeaInLocalDatabase(
                                _tempIdeaTitle.value!!,
                                tempGoalId!!,
                                tempKeyResultId,
                                tempStepId
                            )
                    )
                }.map {
                    ideaInteractor.getIdeaById(it)
                        .subscribe { entity ->
                            ideaInteractor.saveIdeaToRemote(entity)
                        }
                }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { mainFlowNavigation.popBack() },
                        {
                            _status.onNext(Status.ERROR)
                            it.printStackTrace()
                        }
                    )
            )
        } else _status.onNext(Status.ERROR.apply { message = errorMessage })
    }
}
package by.aermakova.todoapp.ui.goal.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.di.scope.NavigationGoals
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.model.FunctionNoArgs
import by.aermakova.todoapp.data.model.FunctionSelect
import by.aermakova.todoapp.data.model.GoalModel
import by.aermakova.todoapp.data.useCase.FindGoalUseCase
import by.aermakova.todoapp.data.useCase.LoadAllGoalsUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.base.DetailsScreen
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Status
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject


class GoalDetailsViewModel @Inject constructor(
    @NavigationGoals private val navigation: MainFlowNavigation,
    private val goalInteractor: GoalInteractor,
    private val loadAllGoalsUseCase: LoadAllGoalsUseCase,
    private val findGoalUseCase: FindGoalUseCase,
    private val goalId: Long
) : BaseViewModel(), DetailsScreen {

    override val mainFlowNavigation: MainFlowNavigation
        get() = navigation

    override val openEditFragment: FunctionNoArgs
        get() = { mainFlowNavigation.navigateToEditElementFragment(goalId) }

    private val _editButtonIsVisible = MutableLiveData<Boolean>(true)
    override val editButtonIsVisible: LiveData<Boolean>
        get() = _editButtonIsVisible

    private val _markedKeyResultIds = arrayListOf<Long>()

    private val keyResultMarkedAsDone: FunctionSelect = { keyResultId, select ->
        if (select) {
            _markedKeyResultIds.add(keyResultId)
        } else {
            _markedKeyResultIds.remove(keyResultId)
        }
        _markKeyResultsAsDoneToggle.postValue(_markedKeyResultIds.isNotEmpty())
    }

    init {
        loadingAction.invoke()
        disposable.add(
            goalInteractor.getGoalWithKeyResultsAndUnattachedTasks(goalId, keyResultMarkedAsDone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _goalModel.postValue(it)
                        _editButtonIsVisible.postValue(it.status)
                        it.goalItemsList?.let { list -> _goalItemsList.onNext(list) }
                        successAction.invoke()
                    },
                    {
                        _status.onNext(Status.ERROR)
                        it.printStackTrace()
                    }
                )
        )
    }

    private val _goalModel = MutableLiveData<GoalModel>()
    val goalModel: LiveData<GoalModel>
        get() = _goalModel

    private val _goalItemsList = PublishSubject.create<List<CommonModel>>()
    val goalItemsList: Observable<List<CommonModel>>
        get() = _goalItemsList

    val markGoalAsDoneToggle = MutableLiveData<Boolean>(false)

    private val _markKeyResultsAsDoneToggle = MutableLiveData<Boolean>(false)
    val markKeyResultsAsDoneToggle: LiveData<Boolean>
        get() = _markKeyResultsAsDoneToggle

    val saveChanges = { saveAllChanges() }

    private fun saveAllChanges() {
        if (markGoalAsDoneToggle.value == true) {
            saveUpdatedGoal()
        } else {
            saveUpdatedKeyResults()
        }
    }

    private fun saveUpdatedGoal() {
        loadingAction.invoke()
        loadAllGoalsUseCase.saveUpdatedGoal(
            goalId,
            disposable,
            { updateGoalToRemote() },
            {
                successAction.invoke()
                mainFlowNavigation.popBack()
            }, errorAction
        )
    }

    private fun saveUpdatedKeyResults() {
        loadingAction.invoke()
        disposable.add(
            Single.create<Boolean> {
                it.onSuccess(goalInteractor.updateKeyResults(true, _markedKeyResultIds))
            }
                .map { updateKeyResultsToRemote(_markedKeyResultIds) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        successAction.invoke()
                        mainFlowNavigation.popBack()
                    },
                    {
                        _status.onNext(Status.ERROR.apply { message = it.message ?: "" })
                        it.printStackTrace()
                    }
                )
        )
    }

    private fun updateKeyResultsToRemote(keyResIds: List<Long>): Disposable {
        return goalInteractor
            .getKeyResultsByIds(keyResIds)
            .subscribe(
                { goalInteractor.updateKeyResultsToRemote(it, keyResIds) },
                { it.printStackTrace() }
            )
    }

    private fun updateGoalToRemote(): Disposable? {
        return findGoalUseCase.useGoalById(goalId, {
            goalInteractor.updateGoalToRemote(it)
        })
    }
}
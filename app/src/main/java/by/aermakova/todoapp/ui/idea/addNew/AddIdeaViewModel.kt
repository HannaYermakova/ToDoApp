package by.aermakova.todoapp.ui.idea.addNew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.db.entity.toCommonModel
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.ui.adapter.TextModel
import by.aermakova.todoapp.ui.adapter.toCommonModel
import by.aermakova.todoapp.ui.adapter.toTextModel
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.dialog.selectItem.goal.SelectGoalDialogNavigation
import by.aermakova.todoapp.ui.dialog.selectItem.keyResult.SelectKeyResultDialogNavigation
import by.aermakova.todoapp.ui.dialog.selectItem.step.SelectStepDialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Named

class AddIdeaViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    @Named("SelectGoal") private val selectGoalDialogNavigation: SelectGoalDialogNavigation,
    @Named("SelectKeyResult") private val selectKeyResDialogNavigation: SelectKeyResultDialogNavigation,
    private val goalInteractor: GoalInteractor,
    private val stepInteractor: StepInteractor,
    private val ideaInteractor: IdeaInteractor
) : BaseViewModel() {

    val popBack = { mainFlowNavigation.popBack() }

    val saveIdea = { saveIdeaToLocalDataBaseAndSyncToRemote() }

    val selectGoal: (String) -> Unit = { selectGoalDialogNavigation.openItemDialog(it) }

    private val _tempIdeaTitle = BehaviorSubject.create<String>()

    private var tempGoalId: Long? = null
    private var tempKeyResultId: Long? = null
    private var tempStepId: Long? = null

    val tempIdeaTitle: Observer<String>
        get() = _tempIdeaTitle

    val selectedGoalObserver: LiveData<Long>?
        get() = selectGoalDialogNavigation.getDialogResult()

    val selectedKeyResObserver: LiveData<Long>?
        get() = selectKeyResDialogNavigation.getDialogResult()

    private val _stepsList = BehaviorSubject.create<List<TextModel>>()
    val stepsList: Observable<List<TextModel>>
        get() = _stepsList

    val stepSelected: (Long) -> Unit = {
        addTempStep(it)
    }

    private val _goalTitle = MutableLiveData<String>()
    val goalTitle: LiveData<String>
        get() = _goalTitle

    private val _goalIsSelected = MutableLiveData<Boolean>(false)
    val goalIsSelected: LiveData<Boolean>
        get() = _goalIsSelected

    private val _keyResultTitle = MutableLiveData<String>()
    val keyResultTitle: LiveData<String>
        get() = _keyResultTitle

    private val _keyResultIsSelected = MutableLiveData<Boolean>(false)
    val keyResultIsSelected: LiveData<Boolean>
        get() = _keyResultIsSelected

    val selectKeyResult: (String) -> Unit =
        { title -> tempGoalId?.let { selectKeyResDialogNavigation.openItemDialog(title, it) } }

    fun addTempGoal(goalId: Long?) {
        goalId?.let {
            tempGoalId = goalId
            _goalIsSelected.postValue(tempGoalId != null)
            disposable.add(
                goalInteractor.getGoalKeyResultsById(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { goalKeyResults ->
                        _goalTitle.postValue(goalKeyResults.goal.text)
                    }
            )
        }
    }

    fun addTempKeyResult(keyResultId: Long?) {
        keyResultId?.let {
            tempKeyResultId = keyResultId
            _keyResultIsSelected.postValue(tempKeyResultId != null)
            disposable.add(
                goalInteractor.getKeyResultsById(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { entity -> entity.toCommonModel() }
                    .subscribe { keyResultKeyResults ->
                        _keyResultTitle.postValue(keyResultKeyResults.text)
                        setSteps(keyResultId)
                    }
            )
        }
    }

    private fun addTempStep(stepId: Long?) {
        tempStepId = stepId
    }

    private fun setSteps(keyResultId: Long) {
        disposable.add(
            stepInteractor.getUndoneStepsByKeyResultId(keyResultId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _stepsList.onNext(it.map { item -> item.toTextModel() }) },
                    { it.printStackTrace() }
                )
        )
    }

    private fun saveIdeaToLocalDataBaseAndSyncToRemote() {
        if (!_tempIdeaTitle.value.isNullOrBlank() && tempGoalId != null) {
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
                    ideaInteractor.getIdeaById(it).subscribe { entity ->
                        ideaInteractor.saveIdeaToRemote(entity)
                    }
                }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { mainFlowNavigation.popBack() },
                        { it.printStackTrace() }
                    )
            )
        }
    }
}
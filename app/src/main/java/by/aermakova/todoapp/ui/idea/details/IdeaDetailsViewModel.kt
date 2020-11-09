package by.aermakova.todoapp.ui.idea.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.model.IdeaModel
import by.aermakova.todoapp.data.model.toCommonModel
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.dialog.convertIdea.ConvertIdeaDialogNavigator
import by.aermakova.todoapp.ui.dialog.selectItem.keyResult.SelectKeyResultDialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class IdeaDetailsViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    @Named("ConvertIdea") private val convertIdeaDialogNavigator: ConvertIdeaDialogNavigator,
    @Named("SelectKeyResult") private val selectKeyResDialogNavigation: SelectKeyResultDialogNavigation,
    private val ideaInteractor: IdeaInteractor,
    private val goalInteractor: GoalInteractor,
    private val stepInteractor: StepInteractor,
    private val ideaId: Long,
    private val selectKeyResultTitle: String
) : BaseViewModel() {

    val popBack = { mainFlowNavigation.popBack() }

    val convertIdeaToStep = { convertIdeaIntoStep() }

    val convertIdeaToTask = { convertIdeaIntoTask() }

    private val _ideaModel = MutableLiveData<IdeaModel>()
    val ideaModel: LiveData<IdeaModel>
        get() = _ideaModel

    private val _goalTitle = MutableLiveData<String>()
    val goalTitle: LiveData<String>
        get() = _goalTitle

    private val _keyResTitle = MutableLiveData<String>()
    val keyResTitle: LiveData<String>
        get() = _keyResTitle

    private val _keyResVisible = MutableLiveData<Boolean>()
    val keyResVisible: LiveData<Boolean>
        get() = _keyResVisible

    private val _stepTitle = MutableLiveData<String>()
    val stepTitle: LiveData<String>
        get() = _stepTitle

    private val _stepVisible = MutableLiveData<Boolean>(false)
    val stepVisible: LiveData<Boolean>
        get() = _stepVisible

    val convertIdeaToTaskObserver: LiveData<Boolean>?
        get() = convertIdeaDialogNavigator.getDialogResult()

    val selectedKeyResObserver: LiveData<Long>?
        get() = selectKeyResDialogNavigation.getDialogResult()

    init {
        disposable.add(
            ideaInteractor
                .getIdeaById(ideaId)
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
                    it.keyResultId?.let { id ->
                        goalInteractor.getKeyResultsById(id)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                { keyRes ->
                                    _keyResVisible.postValue(true)
                                    _keyResTitle.postValue(keyRes.text)
                                },
                                { error -> error.printStackTrace() }
                            )
                    }
                }.doOnNext {
                    it.stepId?.let { id ->
                        stepInteractor.getStepById(id)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                { stepEntity ->
                                    _stepVisible.postValue(true)
                                    _stepTitle.postValue(stepEntity.text)
                                },
                                { error -> error.printStackTrace() }
                            )
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _ideaModel.postValue(it) },
                    { it.printStackTrace() }
                )
        )
    }

    private fun convertIdeaIntoTask() {
        convertIdeaDialogNavigator.openConvertIdeaDialog(ideaId)
    }

    private fun convertIdeaIntoStep() {
        ideaModel.value?.let { ideaModel ->
            ideaModel.keyResultId?.let {
                convertIdeaIntoStepWithKeyResult(ideaModel, it)
            } ?: selectKeyResDialogNavigation.openItemDialog(selectKeyResultTitle, ideaModel.goalId)
        }
    }

    private fun convertIdeaIntoStepWithKeyResult(ideaModel: IdeaModel, keyResultId: Long) {
        disposable.add(
            stepInteractor.createStep(
                ideaModel.text,
                ideaModel.goalId,
                keyResultId
            )
                .doOnSuccess { ideaInteractor.deleteIdea(ideaId) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { mainFlowNavigation.popBack() },
                    { error -> error.printStackTrace() }
                )
        )
    }

    fun saveAndClose(value: Boolean?) {
        value?.let {
            if (value) {
                disposable.add(
                    Single.create<Boolean> { it.onSuccess(true) }
                        .doOnSuccess { ideaInteractor.deleteIdea(ideaId) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { mainFlowNavigation.popBack() },
                            { it.printStackTrace() }
                        )
                )
            }
        }
    }

    fun addKeyResult(keyResultId: Long?) {
        ideaModel.value?.let { ideaModel ->
            keyResultId?.let {
                convertIdeaIntoStepWithKeyResult(ideaModel, it)
            }
        }
    }
}
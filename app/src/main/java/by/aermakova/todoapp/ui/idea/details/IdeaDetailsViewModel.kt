package by.aermakova.todoapp.ui.idea.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.model.IdeaModel
import by.aermakova.todoapp.data.useCase.CreateStepUseCase
import by.aermakova.todoapp.data.useCase.LoadIdeaDetailsUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.dialog.convertIdea.ConvertIdeaDialogNavigator
import by.aermakova.todoapp.ui.dialog.selectItem.keyResult.SelectKeyResultDialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Status
import javax.inject.Inject
import javax.inject.Named


class IdeaDetailsViewModel @Inject constructor(
    private val ideaId: Long,
    private val mainFlowNavigation: MainFlowNavigation,
    private val loadIdeaDetails: LoadIdeaDetailsUseCase,
    private val createStepUseCase: CreateStepUseCase,
    @Named("ConvertIdea") private val convertIdeaDialogNavigator: ConvertIdeaDialogNavigator,
    @Named("SelectKeyResult") private val selectKeyResDialogNavigation: SelectKeyResultDialogNavigation,
    @Named("SelectKeyResultTitle") private val selectKeyResultTitle: String
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
        _status.onNext(Status.LOADING)
        loadIdeaDetails.loadIdeaDetailsById(
            ideaId,
            disposable,
            { _goalTitle.postValue(it) },
            {
                _keyResVisible.postValue(true)
                _keyResTitle.postValue(it)
            },
            {
                _stepVisible.postValue(true)
                _stepTitle.postValue(it)
            },
            {
                _status.onNext(Status.SUCCESS)
                _ideaModel.postValue(it)
            },
            errorAction
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
        createStepUseCase.saveStepToLocalDataBaseAndSyncToRemote(
            disposable,
            ideaModel.text,
            ideaModel.goalId,
            keyResultId,
            { mainFlowNavigation.popBack() }
        )
    }

    fun saveAndClose(value: Boolean?) {
        value?.let {
            if (value) {
                _status.onNext(Status.LOADING)
                loadIdeaDetails.saveIdeaDetails(ideaId,
                    disposable,
                    { mainFlowNavigation.popBack() },
                    errorAction)
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
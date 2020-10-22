package by.aermakova.todoapp.ui.idea.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.ui.adapter.IdeaModel
import by.aermakova.todoapp.ui.adapter.toCommonModel
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.dialog.convertIdea.ConvertIdeaDialogNavigator
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class IdeaDetailsViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    @Named("ConvertIdea") private val convertIdeaDialogNavigator: ConvertIdeaDialogNavigator,
    private val ideaInteractor: IdeaInteractor,
    private val goalInteractor: GoalInteractor,
    private val stepInteractor: StepInteractor,
    private val ideaId: Long
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

    val convertIdeaToTaskObserver: LiveData<Boolean>?
        get() = convertIdeaDialogNavigator.getDialogResult()

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
                    { _ideaModel.postValue(it) },
                    { it.printStackTrace() }
                )
        )
    }

    private fun convertIdeaIntoTask() {
        convertIdeaDialogNavigator.openConvertIdeaDialog(ideaId)
    }

    private fun convertIdeaIntoStep() {
        ideaModel.value?.let {
            disposable.add(
                stepInteractor.createStep(
                    it.text,
                    it.goalId,
                    it.keyResultId
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
    }

    fun saveAndClose(value: Boolean?) {
        value?.let { if (value) mainFlowNavigation.popBack() }
    }
}
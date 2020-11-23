package by.aermakova.todoapp.ui.dialog.convertIdea

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.di.scope.NavigationConvertIdea
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.model.FunctionLong
import by.aermakova.todoapp.data.useCase.CreateTaskUseCase
import by.aermakova.todoapp.data.useCase.KeyResultSelectUseCase
import by.aermakova.todoapp.ui.base.BaseDialogVieModel
import by.aermakova.todoapp.ui.goal.main.INIT_SELECTED_ITEM_ID
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ConvertIdeaIntoTaskViewModel @Inject constructor(
    @NavigationConvertIdea private val convertIdeaDialogNavigator: ConvertIdeaDialogNavigator,
    val keyResultSelectUseCase: KeyResultSelectUseCase,
    val createTaskUseCase: CreateTaskUseCase,
    ideaInteractor: IdeaInteractor,
    ideaId: Long,
) : BaseDialogVieModel() {

    private lateinit var ideaText: String
    private var ideaGoalId: Long = INIT_SELECTED_ITEM_ID

    val saveTask = {
        createTaskUseCase.saveTaskToLocalDataBaseAndSyncToRemote(
            disposable, loadingAction, {
                successAction.invoke()
                convertIdeaDialogNavigator.setDialogResult(it)
                _dismissCommand.onNext(true)
            }, errorAction
        )
    }

    init {
        disposable.add(
            ideaInteractor.getIdeaById(ideaId)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { idea ->
                        ideaText = idea.text
                        ideaGoalId = idea.ideaGoalId
                        if (idea.ideaKeyResultId == null) {
                            _keyResultIsVisible.postValue(true)
                            keyResultSelectUseCase.setKeyResultList(disposable, idea.ideaGoalId)
                        } else {
                            setTaskCreatorFields(idea.ideaKeyResultId)
                        }
                    },
                    { it.printStackTrace() }
                )
        )
    }

    private fun setTaskCreatorFields(ideaKeyResultId: Long) {
        createTaskUseCase.setTaskProperties(
            ideaText,
            ideaGoalId,
            ideaKeyResultId
        )
    }

    private val _keyResultIsVisible = MutableLiveData<Boolean>(false)
    val keyResultIsVisible: LiveData<Boolean>
        get() = _keyResultIsVisible

    val keyResultSelected: FunctionLong = {
        setTaskCreatorFields(it)
    }

    override fun doOnCancel() {
        convertIdeaDialogNavigator.setDialogResult(false)
    }

    override fun doOnOk() {

    }
}
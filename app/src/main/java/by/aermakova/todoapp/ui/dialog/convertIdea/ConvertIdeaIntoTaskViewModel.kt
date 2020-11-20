package by.aermakova.todoapp.ui.dialog.convertIdea

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.di.scope.DialogPickDate
import by.aermakova.todoapp.data.di.scope.NavigationConvertIdea
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.interactor.TaskCreator
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.data.useCase.KeyResultSelectUseCase
import by.aermakova.todoapp.ui.base.BaseDialogVieModel
import by.aermakova.todoapp.ui.dialog.datePicker.PickDayDialogNavigator
import by.aermakova.todoapp.ui.goal.main.INIT_SELECTED_ITEM_ID
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ConvertIdeaIntoTaskViewModel @Inject constructor(
    @DialogPickDate private val pickDayDialogNavigation: PickDayDialogNavigator,
    @NavigationConvertIdea private val convertIdeaDialogNavigator: ConvertIdeaDialogNavigator,
    val keyResultSelectUseCase: KeyResultSelectUseCase,
    taskInteractor: TaskInteractor,
    ideaInteractor: IdeaInteractor,
    ideaId: Long,
    errorMessage: String
) : BaseDialogVieModel() {

    private val saveAndClose = BehaviorSubject.create<Boolean>()
    private lateinit var ideaText: String
    private var ideaGoalId: Long = INIT_SELECTED_ITEM_ID

    val taskCreator = TaskCreator(
        pickDayDialogNavigation,
        taskInteractor,
        disposable,
        saveAndClose,
        _status,
        errorMessage
    )

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
        disposable.add(
            saveAndClose.subscribe {
                convertIdeaDialogNavigator.setDialogResult(it)
                _dismissCommand.onNext(true)
            }
        )
    }

    private fun setTaskCreatorFields(ideaKeyResultId: Long) {
        taskCreator.setTaskProperties(
            ideaText,
            ideaGoalId,
            ideaKeyResultId
        )
    }

    private val _keyResultIsVisible = MutableLiveData<Boolean>(false)
    val keyResultIsVisible: LiveData<Boolean>
        get() = _keyResultIsVisible

    val keyResultSelected: (Long) -> Unit = {
        setTaskCreatorFields(it)
    }

    override fun doOnCancel() {
        convertIdeaDialogNavigator.setDialogResult(false)
    }

    override fun doOnOk() {

    }
}
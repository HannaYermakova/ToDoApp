package by.aermakova.todoapp.ui.dialog.convertIdea

import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.interactor.TaskCreator
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.ui.base.BaseDialogVieModel
import by.aermakova.todoapp.ui.dialog.datePicker.PickDayDialogNavigator
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Named

class ConvertIdeaIntoTaskViewModel @Inject constructor(
    @Named("PickDate") private val pickDayDialogNavigation: PickDayDialogNavigator,
    @Named("ConvertIdea") private val convertIdeaDialogNavigator: ConvertIdeaDialogNavigator,
    private val taskInteractor: TaskInteractor,
    private val ideaInteractor: IdeaInteractor,
    private val ideaId: Long,
    private val errorMessage: String
) : BaseDialogVieModel() {

    private val saveAndClose = BehaviorSubject.create<Boolean>()

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
                        //TODO remove this check
                        idea.ideaKeyResultId?.let {
                            taskCreator.setTaskProperties(
                                idea.text,
                                idea.ideaGoalId,
                                idea.ideaKeyResultId
                            )
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

    override fun doOnCancel() {
        convertIdeaDialogNavigator.setDialogResult(false)
    }

    override fun doOnOk() {

    }
}
package by.aermakova.todoapp.ui.dialog.convertIdea

import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.interactor.TaskCreator
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.dialog.datePicker.PickDayDialogNavigator
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Named

class ConvertIdeaIntoTaskViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    @Named("PickDate") private val pickDayDialogNavigation: PickDayDialogNavigator,
    @Named("ConvertIdea") private val convertIdeaDialogNavigator: ConvertIdeaDialogNavigator,
    private val taskInteractor: TaskInteractor,
    private val ideaInteractor: IdeaInteractor,
    private val ideaId: Long
) : BaseViewModel() {

    private val saveAndClose = BehaviorSubject.create<Boolean>()

    val taskCreator = TaskCreator(
        pickDayDialogNavigation,
        taskInteractor,
        disposable,
        saveAndClose
    )

    init {
        disposable.add(
            ideaInteractor.getIdeaById(ideaId)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { idea ->
                        taskCreator.setTaskProperties(
                            idea.text,
                            idea.ideaGoalId,
                            idea.ideaKeyResultId
                        )
                    },
                    { it.printStackTrace() }
                )
        )
        disposable.add(
            saveAndClose.subscribe {
                convertIdeaDialogNavigator.setDialogResult(it)
                mainFlowNavigation.popBack()
            }
        )
    }

    val cancel = { convertIdeaDialogNavigator.setDialogResult(false) }
}
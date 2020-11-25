package by.aermakova.todoapp.data.useCase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.db.entity.Interval
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.data.model.FunctionNoArgs
import by.aermakova.todoapp.data.model.FunctionString
import by.aermakova.todoapp.ui.dialog.datePicker.PickDayDialogNavigator
import by.aermakova.todoapp.util.handleError
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CreateTaskUseCase(
    private val pickDayDialogNavigation: PickDayDialogNavigator,
    private val taskInteractor: TaskInteractor,
    private val errorMessage: String
) {

    var tempTaskTitle: String = ""

    var tempGoalId: Long? = null

    var tempKeyResultId: Long? = null

    var tempStepId: Long? = null

    val scheduledTask = MutableLiveData<Boolean>(false)

    val taskInterval = MutableLiveData<Interval>()

    val deadlinedTask = MutableLiveData<Boolean>()

    val taskFinishTime = MutableLiveData<Long>()

    fun checkAndSetFinishTime(finishTime: Long?) {
        if (finishTime != null && finishTime > System.currentTimeMillis()) {
            taskFinishTime.postValue(finishTime)
        }
    }

    val pickFinishDay = { pickDayDialogNavigation.openItemDialog("") }

    val selectedFinishDateObserver: LiveData<Long>?
        get() = pickDayDialogNavigation.getDialogResult()

    fun saveTaskToLocalDataBaseAndSyncToRemote(
        disposable: CompositeDisposable,
        loadingAction: FunctionNoArgs,
        successAction: (Boolean) -> Unit,
        errorAction: FunctionString? = null
    ) {
        if (!tempTaskTitle.isBlank()) {
            loadingAction.invoke()
            disposable.add(
                Single.create<Long> {
                    it.onSuccess(
                        taskInteractor.saveTaskInLocalDatabase(
                            tempTaskTitle,
                            tempGoalId,
                            tempKeyResultId,
                            tempStepId,
                            finishDate = if (deadlinedTask.value!!) {
                                taskFinishTime.value
                            } else null,
                            scheduledTask = scheduledTask.value!!,
                            interval = if (scheduledTask.value!!) taskInterval.value else null
                        )
                    )
                }
                    .map {
                        taskInteractor.getTaskById(it).firstElement().subscribe { entity ->
                            taskInteractor.saveTaskToRemote(entity)
                        }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { successAction.invoke(true) },
                        { it.handleError(errorMessage, errorAction) }
                    )
            )
        } else {
            loadingAction.invoke()
        }
    }

    fun setTaskProperties(text: String, ideaGoalId: Long, ideaKeyResultId: Long) {
        tempTaskTitle = text
        tempGoalId = ideaGoalId
        tempKeyResultId = ideaKeyResultId
    }
}
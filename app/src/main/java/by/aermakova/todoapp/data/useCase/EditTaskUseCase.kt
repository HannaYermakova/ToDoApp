package by.aermakova.todoapp.data.useCase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.db.entity.Interval
import by.aermakova.todoapp.data.db.entity.TaskEntity
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.ui.dialog.datePicker.PickDayDialogNavigator
import by.aermakova.todoapp.util.handleError
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class EditTaskUseCase(
    private val taskInteractor: TaskInteractor,
    private val pickDayDialogNavigation: PickDayDialogNavigator,
    private val errorMessage: String
) {

    val newTitle = MutableLiveData<String>()

    val scheduled = MutableLiveData<Boolean>()

    val taskInterval = MutableLiveData<Interval>()

    val taskFinishTime = MutableLiveData<Long>()

    val pickFinishDay = { pickDayDialogNavigation.openItemDialog("") }

    val selectedFinishDateObserver: LiveData<Long>?
        get() = pickDayDialogNavigation.getDialogResult()

    val finishDateIsSelected = MutableLiveData<Boolean>(false)

    fun checkAndSetFinishTime(finishTime: Long?) {
        if (finishTime != null && finishTime > System.currentTimeMillis()) {
            finishDateIsSelected.postValue(true)
            taskFinishTime.postValue(finishTime)
        }
    }

    fun saveUpdatedTaskLocal(
        disposable: CompositeDisposable,
        task: TaskEntity,
        loadingAction: () -> Unit,
        successAction: () -> Unit,
        errorAction: (String) -> Unit
    ) {
        loadingAction.invoke()
        disposable.add(
            Single.create<Long> {
                it.onSuccess(with(task) {
                    val title = newTitle.value ?: text
                    val tempFinishDate = taskFinishTime.value ?: finishDate
                    val tempScheduledTask = scheduled.value ?: scheduledTask
                    val tempTaskInterval = if (tempScheduledTask) {
                        taskInterval.value?.code ?: interval
                    } else null

                    taskInteractor.updateTaskInLocalDatabase(
                        taskId,
                        title,
                        taskGoalId,
                        taskKeyResultId,
                        taskStepId,
                        startTime,
                        tempFinishDate,
                        tempScheduledTask,
                        tempTaskInterval
                    )
                })
            }
                .map {
                    taskInteractor.getTaskById(it).subscribe { entity ->
                        taskInteractor.saveTaskToRemote(entity)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { successAction.invoke() },
                    { it.handleError(errorMessage, errorAction) }
                )
        )
    }
}
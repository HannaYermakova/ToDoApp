package by.aermakova.todoapp.data.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.db.entity.Interval
import by.aermakova.todoapp.ui.dialog.datePicker.PickDayDialogNavigator
import by.aermakova.todoapp.util.convertLongToDate
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TaskCreator(
    private val pickDayDialogNavigation: PickDayDialogNavigator,
    private val taskInteractor: TaskInteractor,
    private val disposable: CompositeDisposable,
    private val saveAndClose: Observer<Boolean>
) {

    var tempTaskTitle: String = ""

    var tempGoalId: Long? = null

    var tempKeyResultId: Long? = null

    var tempStepId: Long? = null

    val scheduledTask = MutableLiveData<Boolean>()

    val taskInterval = MutableLiveData<Interval>()

    val deadlinedTask = MutableLiveData<Boolean>()

    private var tempFinishTime: Long? = null

    private val _finishDateIsSelected = MutableLiveData<Boolean>(false)
    val finishDateIsSelected: LiveData<Boolean>
        get() = _finishDateIsSelected

    private val _finishDateText = MutableLiveData<String>()
    val finishDateText: LiveData<String>
        get() = _finishDateText

    fun checkAndSetFinishTime(finishTime: Long?) {
        if (finishTime != null && finishTime > System.currentTimeMillis()) {
            _finishDateIsSelected.postValue(true)
            _finishDateText.postValue(convertLongToDate(finishTime))
            tempFinishTime = finishTime
        }
    }

    val saveTask = { saveTaskToLocalDataBaseAndSyncToRemote() }

    val pickFinishDay = { pickDayDialogNavigation.openItemDialog("") }

    val selectedFinishDateObserver: LiveData<Long>?
        get() = pickDayDialogNavigation.getDialogResult()

    private fun saveTaskToLocalDataBaseAndSyncToRemote() {
        if (!tempTaskTitle.isBlank() && scheduledTask.value != null) {
            disposable.add(
                Single.create<Long> {
                    it.onSuccess(
                        taskInteractor.saveTaskInLocalDatabase(
                            tempTaskTitle,
                            tempGoalId,
                            tempKeyResultId,
                            tempStepId,
                            finishDate = if (deadlinedTask.value!!) {
                                tempFinishTime
                            } else null,
                            scheduledTask = scheduledTask.value!!,
                            interval = if (scheduledTask.value!!) taskInterval.value else null
                        )
                    )
                }
                    .map {
                        taskInteractor.getTaskById(it).subscribe { entity ->
                            taskInteractor.saveTaskToRemote(entity)
                        }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { saveAndClose.onNext(true) },
                        { it.printStackTrace() }
                    )
            )
        }
    }

    fun setTaskProperties(text: String, ideaGoalId: Long, ideaKeyResultId: Long) {
        tempTaskTitle = text
        tempGoalId = ideaGoalId
        tempKeyResultId = ideaKeyResultId
    }
}
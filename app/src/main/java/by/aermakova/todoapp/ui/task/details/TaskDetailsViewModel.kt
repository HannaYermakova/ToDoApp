package by.aermakova.todoapp.ui.task.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.ui.adapter.TaskModel
import by.aermakova.todoapp.ui.adapter.toCommonModel
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TaskDetailsViewModel @Inject constructor(
    private val navigation: MainFlowNavigation,
    private val taskInteractor: TaskInteractor,
    private val taskId: Long
) : BaseViewModel() {

    val popBack = { navigation.popBack() }

    private val _taskModel = MutableLiveData<TaskModel>()
    val taskModel: LiveData<TaskModel>
        get() = _taskModel

    init {
        disposable.add(
            taskInteractor
                .getTaskById(taskId)
                .map { it.toCommonModel { } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _taskModel.postValue(it) },
                    { it.printStackTrace() }
                )
        )
    }
}
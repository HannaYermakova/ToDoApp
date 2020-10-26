package by.aermakova.todoapp.ui.task.main

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.db.entity.TaskEntity
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.databinding.FilterBottomSheetBinding
import by.aermakova.todoapp.ui.adapter.CommonModel
import by.aermakova.todoapp.ui.adapter.toCommonModel
import by.aermakova.todoapp.ui.adapter.toTextModel
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Status
import by.aermakova.todoapp.util.TaskFilterItem
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    private val navigation: MainFlowNavigation,
    private val taskInteractor: TaskInteractor,
    private val resources: Resources,
    private val bind: FilterBottomSheetBinding,
    private val filterDialog: BottomSheetDialog

) : BaseViewModel() {

    val addNewElement = { navigation.navigateToAddNewElementFragment() }

    val openFilterDialog = { filterDialog.show() }

    private val _tasksList = PublishSubject.create<List<CommonModel>>()
    val tasksList: Observable<List<CommonModel>>
        get() = _tasksList.hide()

    private val _filterTitle = MutableLiveData<String>(resources.getString(TaskFilterItem.ALL_TASKS.titleText))
    val filterTitle: LiveData<String>
        get() = _filterTitle

    val filterItems: List<CommonModel> =
        taskInteractor.getFilterItems()
            .map { filter ->
                filter.toTextModel(resources) {
                    filterDialog.dismiss()
                    loadTasks(filter)
                    _filterTitle.postValue(resources.getString(filter.titleText))
                }
            }

    init {
        initFilterList()
        loadTasks(TaskFilterItem.ALL_TASKS)
    }

    private fun loadTasks(filter: TaskFilterItem) {
        _status.onNext(Status.LOADING)
        disposable.add(
            taskInteractor.getAllTasks()
                .concatMapSingle {
                    Observable.fromIterable(it).filter {
                        filterTasksList(filter, it)
                    }
                        .toList()
                }
                .map { list ->
                    list.map { it.toCommonModel { id -> navigation.navigateToShowDetailsFragment(id) } }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _tasksList.onNext(it)
                        _status.onNext(Status.SUCCESS)
                    },
                    {
                        it.printStackTrace()
                        _status.onNext(Status.ERROR)
                    }
                )
        )
    }

    private fun filterTasksList(filter: TaskFilterItem, task: TaskEntity): Boolean {

        val deadline = task.finishDate
        deadline?.let {
            val today = Calendar.getInstance()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = deadline
            Log.d("A_TasksViewModel", "today ${today.get(Calendar.DATE)}")
            Log.d("A_TasksViewModel", "deadline ${calendar.get(Calendar.DATE)}")

            return when (filter) {
                TaskFilterItem.TODAY -> {
                    !task.taskStatusDone && today.get(Calendar.DATE) == calendar.get(Calendar.DATE)
                }
                TaskFilterItem.TOMORROW -> {
                    !task.taskStatusDone && today.get(Calendar.DATE) + 1 == calendar.get(Calendar.DATE)
                }
                TaskFilterItem.THIS_WEEK -> {
                    !task.taskStatusDone && calendar.get(Calendar.DATE) - today.get(Calendar.DATE) < 7
                }
                TaskFilterItem.NEXT_WEEK -> {
                    !task.taskStatusDone &&
                            calendar.get(Calendar.DATE) - today.get(Calendar.DATE) in 7..13
                }
                TaskFilterItem.SOMEDAY -> !task.taskStatusDone
                TaskFilterItem.DONE -> task.taskStatusDone
                TaskFilterItem.LATER -> {
                    !task.taskStatusDone && calendar.get(Calendar.DATE) - today.get(Calendar.DATE) > 13
                }
                else -> true
            }
        }

        return when (filter) {
            TaskFilterItem.SOMEDAY -> task.finishDate == null
            TaskFilterItem.DONE -> task.taskStatusDone
            TaskFilterItem.ALL_TASKS -> true
            else -> false
        }
    }

    private fun initFilterList() {
        bind.viewModel = this
        filterDialog.setContentView(bind.root)
    }
}
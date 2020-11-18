package by.aermakova.todoapp.ui.task.main

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.di.scope.NavigationTasks
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.model.TextModel
import by.aermakova.todoapp.data.model.toCommonModel
import by.aermakova.todoapp.data.model.toTextModel
import by.aermakova.todoapp.data.useCase.TaskBottomSheetMenuUseCase
import by.aermakova.todoapp.databinding.BottomSheetFilterTaskBinding
import by.aermakova.todoapp.databinding.BottomSheetSortTaskBinding
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.task.TasksNavigation
import by.aermakova.todoapp.util.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Named

class TasksViewModel @Inject constructor(
    @Named("FilterTask") private val filterBind: BottomSheetFilterTaskBinding,
    @Named("SortTask") private val sortBind: BottomSheetSortTaskBinding,
    @NavigationTasks private val navigation: TasksNavigation,
    private val taskInteractor: TaskInteractor,
    private val resources: Resources,
    private val dialog: BottomSheetDialog,
    private val taskBottomSheetMenuUseCase: TaskBottomSheetMenuUseCase
) : BaseViewModel() {

    val addNewElement = { navigation.navigateToAddNewElementFragment(item = Item.GOAL) }

    val actionItems: LiveData<List<CommonModel>> =
        taskBottomSheetMenuUseCase.getLiveListOfStepActionsItems(disposable, errorAction)

    val openFilterDialog = {
        initFilterList()
        dialog.show()
    }

    val openSortDialog = {
        initSortList()
        dialog.show()
    }

    private val _tasksList = PublishSubject.create<List<CommonModel>>()
    val tasksList: Observable<List<CommonModel>>
        get() = _tasksList.hide()

    private var _currentFilter = TaskFilterItem.ALL_TASKS

    private var _currentSortCondition = TaskSortItem.BY_START_DATE

    private val _filterTitle =
        MutableLiveData<String>(resources.getString(_currentFilter.titleText))
    val filterTitle: LiveData<String>
        get() = _filterTitle

    val filterItems: LiveData<List<CommonModel>> = getLiveListOfFilterItems()

    val sortItems: LiveData<List<CommonModel>> = getLiveListOfSortItems()

    private fun getLiveListOfSortItems(): LiveData<List<CommonModel>> {
        val liveList = MutableLiveData<List<CommonModel>>()
        val list = taskInteractor.getSortItems()
            .map { condition ->
                condition.toTextModel(resources, selected = (_currentSortCondition == condition)) {
                    dialog.dismiss()
                    _currentSortCondition = condition
                    setTextItemSelectedById(it, sortItems)
                    loadTasks()
                }
            }
        liveList.postValue(list)
        return liveList
    }

    private fun getLiveListOfFilterItems(): LiveData<List<CommonModel>> {
        val liveList = MutableLiveData<List<CommonModel>>()
        val list = taskInteractor.getFilterItems()
            .map { filter ->
                filter.toTextModel(resources, selected = (_currentFilter == filter)) {
                    dialog.dismiss()
                    _filterTitle.postValue(resources.getString(filter.titleText))
                    _currentFilter = filter
                    setTextItemSelectedById(it, filterItems)
                    loadTasks()
                }
            }
        liveList.postValue(list)
        return liveList
    }

    private fun setTextItemSelectedById(id: Long, list: LiveData<List<CommonModel>>) {
        list.value?.forEach {
            (it as TextModel).selected = it.id == id
        }
    }

    init {
        loadTasks()
    }

    private fun loadTasks() {
        loadingAction.invoke()
        disposable.add(
            taskInteractor.getAllTasks()
                .concatMapSingle { list ->
                    Observable.fromIterable(list).filter { _currentFilter.filterTasksList(it) }
                        .toSortedList(_currentSortCondition.createTasksComparator())
                }
                .map { list ->
                    list.map {
                        it.toCommonModel(
                            { id -> navigation.navigateToShowDetailsFragment(id) },
                            { id -> taskBottomSheetMenuUseCase.openBottomSheetActions(id, this) }
                        )
                    }
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

    private fun initFilterList() {
        filterBind.viewModel = this
        dialog.setContentView(filterBind.root)
    }

    private fun initSortList() {
        sortBind.viewModel = this
        dialog.setContentView(sortBind.root)
    }
}
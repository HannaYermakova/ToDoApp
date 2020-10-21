package by.aermakova.todoapp.ui.task.main

import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.ui.adapter.CommonModel
import by.aermakova.todoapp.ui.adapter.toCommonModel
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    private val navigation: MainFlowNavigation,
    private val taskInteractor: TaskInteractor
) : BaseViewModel() {

    val addNewElement = { navigation.navigateToAddNewElementFragment() }

    private val _tasksList = PublishSubject.create<List<CommonModel>>()
    val tasksList: Observable<List<CommonModel>>
        get() = _tasksList.hide()

    init {
        disposable.add(
            taskInteractor.getAllTasks().map { list ->
                list.map { it.toCommonModel { id -> navigation.navigateToShowDetailsFragment(id) } }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _tasksList.onNext(it) },
                    { it.printStackTrace() }
                )
        )
    }
}
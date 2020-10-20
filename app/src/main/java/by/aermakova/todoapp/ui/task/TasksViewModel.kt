package by.aermakova.todoapp.ui.task

import by.aermakova.todoapp.ui.adapter.CommonModel
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    private val navigation: MainFlowNavigation
) : BaseViewModel() {

    val addNewElement = { navigation.navigateToAddNewElementFragment() }

    private val _tasksList = PublishSubject.create<List<CommonModel>>()
    val tasksList: Observable<List<CommonModel>>
        get() = _tasksList.hide()
}
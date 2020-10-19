package by.aermakova.todoapp.ui.task.addNew

import android.util.Log
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.Observer
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class AddTaskViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation
) : BaseViewModel() {

    val popBack = { mainFlowNavigation.popBack() }

    private val _tempTaskTitle = BehaviorSubject.create<String>()
    val tempTaskTitle: Observer<String>
        get() = _tempTaskTitle

    val saveTask = { saveTaskToLocalDataBaseAndSyncToRemote() }

    private fun saveTaskToLocalDataBaseAndSyncToRemote() {
        mainFlowNavigation.popBack()
    }
}
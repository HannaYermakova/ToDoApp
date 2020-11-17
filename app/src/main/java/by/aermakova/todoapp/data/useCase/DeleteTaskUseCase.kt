package by.aermakova.todoapp.data.useCase

import android.util.Log
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.ui.goal.main.INIT_SELECTED_ITEM_ID
import by.aermakova.todoapp.util.handleError
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DeleteTaskUseCase(
    private val taskInteractor: TaskInteractor,
    private val errorMessage: String
) {

    private var taskId: Long = INIT_SELECTED_ITEM_ID
    private lateinit var errorAction: (String) -> Unit

    fun deleteById(
        taskId: Long,
        disposable: CompositeDisposable,
        errorAction: (String) -> Unit
    ) {
        this.taskId = taskId
        this.errorAction = errorAction
        disposable.add(
            taskInteractor.deleteTaskByIdRemote(taskId)
                .subscribeOn(Schedulers.io())
                .doOnSuccess { taskInteractor.deleteTaskByIdLocal(taskId) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { Log.d("A_DeleteTaskUseCase", "Task has been deleted") },
                    { it.handleError(errorMessage, errorAction) }
                )
        )
    }
}
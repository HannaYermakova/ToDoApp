package by.aermakova.todoapp.data.useCase.delete

import android.util.Log
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.util.handleError
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DeleteTaskUseCase(
    private val taskInteractor: TaskInteractor,
    private val errorMessage: String,
    dialogNavigation: DialogNavigation<Boolean>,
    dialogTitle: String
) : DeleteItemUseCase(dialogNavigation, dialogTitle) {


    override fun deleteById() {
        disposable.add(
            taskInteractor.deleteTaskByIdRemote(itemId)
                .subscribeOn(Schedulers.io())
                .doOnSuccess { taskInteractor.deleteTaskByIdLocal(itemId) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { Log.d("A_DeleteTaskUseCase", "Task has been deleted") },
                    { it.handleError(errorMessage, errorAction) }
                )
        )
    }
}
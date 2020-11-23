package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.db.entity.TaskEntity
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.data.model.TaskTextModel
import by.aermakova.todoapp.data.model.TextModel
import by.aermakova.todoapp.data.model.toTaskTextModel
import by.aermakova.todoapp.data.model.toTextModel
import by.aermakova.todoapp.util.handleError
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FindTaskUseCase(
    private val taskInteractor: TaskInteractor
) {

    fun useTextTasksListByStepId(
        taskId: Long?,
        successAction: (List<TextModel>) -> Unit,
        errorAction: ((String?) -> Unit)? = null
    ): Disposable? {
        return taskId?.let {
            taskInteractor
                .getTaskByStepId(taskId)
                .observeEntitiesList(successAction, errorAction) {
                    it.toTextModel()
                }
        }
    }

    fun useTasksListByStepId(
        taskId: Long?,
        successAction: (List<TaskTextModel>) -> Unit,
        errorAction: ((String) -> Unit)? = null
    ): Disposable? {
        return taskId?.let {
            taskInteractor
                .getTaskByStepId(taskId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { successAction.invoke(it.map { entity -> entity.toTaskTextModel() }) },
                    { it.handleError(it.message, errorAction) }
                )
        }
    }

    fun useTasksById(
        disposable: CompositeDisposable,
        taskId: Long,
        function: (TaskEntity) -> Unit,
        errorAction: (String) -> Unit
    ) {
        disposable.add(
            taskInteractor
                .getTaskById(taskId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { function.invoke(it) },
                    { it.handleError(it.message, errorAction) }
                ))
    }
}

fun <Entity> Single<List<Entity>>.observeEntitiesList(
    successAction: (List<TextModel>) -> Unit,
    errorAction: ((String?) -> Unit)? = null,
    mapEntityToTextModel: (Entity) -> TextModel
): Disposable {
    return observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { list ->
                list.map { mapEntityToTextModel(it) }.let {
                    successAction.invoke(it)
                }
            },
            { it.handleError(it.message, errorAction) }
        )
}
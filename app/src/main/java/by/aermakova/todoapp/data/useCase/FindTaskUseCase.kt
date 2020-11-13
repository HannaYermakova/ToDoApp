package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.data.model.TextModel
import by.aermakova.todoapp.data.model.toTextModel
import by.aermakova.todoapp.util.handleError
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class FindTaskUseCase(
    private val taskInteractor: TaskInteractor
) {

    fun useTasksListByStepId(
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
package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.model.IdeaModel
import by.aermakova.todoapp.data.model.TextModel
import by.aermakova.todoapp.data.model.toCommonModel
import by.aermakova.todoapp.data.model.toTextModel
import by.aermakova.todoapp.util.handleError
import io.reactivex.android.schedulers.AndroidSchedulers

class FindIdeaUseCase(
    private val ideaInteractor: IdeaInteractor
) {

    fun useTextIdeasListByStepId(
        stepId: Long?,
        successAction: (List<TextModel>) -> Unit,
        errorAction: ((String?) -> Unit)? = null
    ) = stepId?.let {
        ideaInteractor.getIdeasByStepId(stepId)
            .observeEntitiesList(successAction, errorAction) {
                it.toTextModel()
            }
    }

    fun useIdeasListByStepId(
        stepId: Long?,
        successAction: (List<IdeaModel>) -> Unit,
        errorAction: ((String?) -> Unit)? = null
    ) = stepId?.let {
        ideaInteractor.getIdeasByStepId(stepId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { successAction.invoke(it.map { it.toCommonModel() }) },
                { it.handleError(it.message, errorAction) }
            )
    }
}
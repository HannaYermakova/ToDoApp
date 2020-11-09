package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.model.TextModel
import by.aermakova.todoapp.data.model.toTextModel

class FindIdeaUseCase(
    private val ideaInteractor: IdeaInteractor
) {

    fun useIdeasListByStepId(
        stepId: Long?,
        successAction: (List<TextModel>) -> Unit,
        errorAction: ((String?) -> Unit)? = null
    ) = stepId?.let {
        ideaInteractor.getIdeasByStepId(stepId)
            .observeEntitiesList(successAction, errorAction) {
                it.toTextModel()
            }
    }
}
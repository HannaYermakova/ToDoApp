package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.db.entity.IdeaEntity
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.model.*
import by.aermakova.todoapp.util.handleError
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

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

    fun useIdeasById(
        disposable: CompositeDisposable,
        ideaId: Long?,
        successAction: (IdeaEntity) -> Unit,
        errorAction: FunctionString
    ) {
        ideaId?.let {
            disposable.add(
                ideaInteractor.getIdeaById(ideaId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { successAction.invoke( it) },
                        { it.handleError(it.message, errorAction) }
                    )
            )
        }
    }
}
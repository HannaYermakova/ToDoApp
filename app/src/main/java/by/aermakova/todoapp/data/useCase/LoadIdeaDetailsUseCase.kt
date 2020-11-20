package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.model.IdeaModel
import by.aermakova.todoapp.data.model.toCommonModel
import by.aermakova.todoapp.util.handleError
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class LoadIdeaDetailsUseCase(
    private val ideaInteractor: IdeaInteractor,
    private val findGoal: FindGoalUseCase,
    private val findStep: FindStepUseCase,
    private val errorMessage: String
) {

    fun loadIdeaDetailsById(
        ideaId: Long,
        disposable: CompositeDisposable,
        successGoalLoad: (String) -> Unit,
        successKeyResultLoad: (String) -> Unit,
        successStepLoad: (String) -> Unit,
        successIdeaLoad: (IdeaModel) -> Unit,
        errorAction: (String) -> Unit
    ) {
        disposable.add(
            ideaInteractor
                .getIdeaById(ideaId)
                .map { it.toCommonModel() }
                .doOnNext {
                    findGoal.useGoalById(it.goalId, { goal ->
                        successGoalLoad.invoke(goal.text)
                    })
                }
                .doOnNext {
                    findGoal.useKeyResultById(it.keyResultId, { keyRes ->
                        successKeyResultLoad.invoke(keyRes.text)
                    })
                }.doOnNext {
                    findStep.useStepByIdInUiThread(it.stepId, { stepEntity ->
                        successStepLoad.invoke(stepEntity.text)
                    }, errorAction)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { successIdeaLoad.invoke(it) },
                    { it.handleError(errorMessage, errorAction) }
                )
        )
    }

    fun saveIdeaDetails(
        ideaId: Long,
        disposable: CompositeDisposable,
        successAction: () -> Unit,
        errorAction: (String) -> Unit
    ) {
        disposable.add(
            Single.create<Boolean> { it.onSuccess(true) }
                .doOnSuccess {
                    ideaInteractor.deleteIdea(ideaId) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { successAction.invoke() },
                    { it.handleError(errorMessage, errorAction) }
                )
        )
    }
}
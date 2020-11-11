package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.data.model.StepModel
import by.aermakova.todoapp.data.model.TextModel
import by.aermakova.todoapp.data.model.toCommonModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoadStepUseCase(
    private val stepInteractor: StepInteractor,
    private val tasksInteractor: TaskInteractor,
    private val findGoal: FindGoalUseCase,
    private val findTask: FindTaskUseCase,
    private val findIdea: FindIdeaUseCase,
    private val stepId: Long,
    private val errorMessage: String
) {

    fun loadStep(
        disposable: CompositeDisposable,
        successGoalLoad: (String) -> Unit,
        successKeyResultLoad: (String) -> Unit,
        successTasksAction: (List<TextModel>) -> Unit,
        successIdeasAction: (List<TextModel>) -> Unit,
        successAction: (StepModel) -> Unit,
        errorAction: (String) -> Unit
    ) {
        disposable.add(
            stepInteractor
                .getStepById(stepId)
                .map { it.toCommonModel { } }
                .doOnSuccess { setGoalTitle(it, successGoalLoad) }
                .doOnSuccess { setKeyResultTitle(it, successKeyResultLoad) }
                .doOnSuccess { setTasksListAsText(it, successTasksAction) }
                .doOnSuccess { setIdeasListAsText(it, successIdeasAction) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        successAction.invoke(it)
                    },
                    {
                        errorAction.invoke(errorMessage)
                        it.printStackTrace()
                    }
                )
        )
    }

    fun markStepAsDone(
        disposable: CompositeDisposable,
        stepId: Long,
        status: Boolean,
        successAction: () -> Unit,
        errorAction: (String) -> Unit
    ) {
        disposable.add(
            Single.create<Boolean> {
                it.onSuccess(stepInteractor.updateStep(status, stepId))
            }
                .map { markTasksAsDone() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        successAction.invoke()

                        /*  _status.onNext(Status.SUCCESS)
                          mainFlowNavigation.popBack()*/
                    },
                    {
                        errorAction.invoke(errorMessage)
                        /* _status.onNext(Status.ERROR)
                         it.printStackTrace()*/
                    }
                )
        )
    }

    private fun markTasksAsDone(): Disposable {
        return stepInteractor
            .getStepById(stepId)
            .subscribe(
                { entity ->
                    stepInteractor.updateStepToRemote(entity)
                    tasksInteractor.markStepsTasksAsDone(true, stepId)
                },
                { it.printStackTrace() }
            )
    }


    private fun setGoalTitle(step: StepModel, loadGoalAction: (String) -> Unit) =
        findGoal.useGoalById(step.goalId, {
            loadGoalAction.invoke(it.text)
        })

    private fun setKeyResultTitle(step: StepModel, loadKeyResultAction: (String) -> Unit) =
        findGoal.useKeyResultById(step.keyResultId, {
            loadKeyResultAction.invoke(it.text)
        })

    private fun setTasksListAsText(step: StepModel, loadTasksAction: (List<TextModel>) -> Unit) =
        findTask.useTasksListByStepId(step.stepId, {
            loadTasksAction.invoke(it)
        })

    private fun setIdeasListAsText(step: StepModel, loadIdeasAction: (List<TextModel>) -> Unit) =
        findIdea.useIdeasListByStepId(step.stepId, {
            loadIdeasAction.invoke(it)
        })
}
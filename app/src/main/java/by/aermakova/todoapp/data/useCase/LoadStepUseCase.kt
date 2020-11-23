package by.aermakova.todoapp.data.useCase

import android.util.Log
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.data.model.*
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
        successGoalLoad: FunctionString,
        successKeyResultLoad: FunctionString,
        successTasksAction: (List<TaskTextModel>) -> Unit,
        successIdeasAction: (List<IdeaModel>) -> Unit,
        successAction: (StepModel) -> Unit,
        errorAction: FunctionString
    ) {
        disposable.add(
            stepInteractor
                .getStepById(stepId)
                .map { it.toCommonModel { } }
                .doOnSuccess { setGoalTitle(it, successGoalLoad) }
                .doOnSuccess { setKeyResultTitle(it, successKeyResultLoad) }
                .doOnSuccess { setTasksList(it, successTasksAction) }
                .doOnSuccess { setIdeasList(it, successIdeasAction) }
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
        errorAction: FunctionString
    ) {
        disposable.add(
            Single.create<Boolean> {
                it.onSuccess(stepInteractor.updateStep(status, stepId))
            }
                .map { markTasksAsDone() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {successAction.invoke() },
                    {errorAction.invoke(errorMessage) }
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

    private fun setGoalTitle(step: StepModel, loadGoalAction: FunctionString) =
        findGoal.useGoalById(step.goalId, {
            loadGoalAction.invoke(it.text)
        })

    private fun setKeyResultTitle(step: StepModel, loadKeyResultAction: FunctionString) =
        findGoal.useKeyResultById(step.keyResultId, {
            loadKeyResultAction.invoke(it.text)
        })

    private fun setTasksList(step: StepModel, loadTasksAction: (List<TaskTextModel>) -> Unit) =
        findTask.useTasksListByStepId(step.stepId, {
            loadTasksAction.invoke(it)
        })

    private fun setIdeasList(step: StepModel, loadIdeasAction: (List<IdeaModel>) -> Unit) =
        findIdea.useIdeasListByStepId(step.stepId, {
            loadIdeasAction.invoke(it)
        })
}
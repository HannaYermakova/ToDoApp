package by.aermakova.todoapp.data.useCase

import android.util.Log
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.data.remote.DeleteStepItems
import by.aermakova.todoapp.ui.goal.main.INIT_SELECTED_ITEM_ID
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DeleteStepUseCase(
    private val goalInteractor: GoalInteractor,
    private val stepInteractor: StepInteractor,
    private val taskInteractor: TaskInteractor,
    private val ideaInteractor: IdeaInteractor,
    private val errorMessage: String
) {

    private var stepId: Long = INIT_SELECTED_ITEM_ID
    private lateinit var errorAction: (String) -> Unit

    fun deleteById(
        stepId: Long,
        disposable: CompositeDisposable,
        errorAction: (String) -> Unit
    ) {
        this.stepId = stepId
        this.errorAction = errorAction
        disposable.add(
            deleteItemsFromRemote(taskInteractor) {
                deleteItemsFromRemote(ideaInteractor) {
                    stepInteractor.deleteStepByIdRemote(stepId)
                        .compose { goalInteractor.deleteStepAndAllItsItemsLocal(stepId) }
                        .subscribe(
                            {
                                Log.d(
                                    "A_DeleteGoalUseCase",
                                    "Goal and all its items hab been deleted"
                                )
                            },
                            {
                                it.printStackTrace()
                                errorAction.invoke(errorMessage)
                            }
                        )
                }
            }


        )
    }

    private fun deleteItemsFromRemote(
        deleteStepItems: DeleteStepItems,
        nextAction: () -> Unit
    ) = deleteStepItems.deleteStepItemsById(stepId)
        .subscribeOn(Schedulers.io())
        .subscribe(
            { nextAction.invoke() },
            {
                it.printStackTrace()
                errorAction.invoke(errorMessage)
            }
        )
}
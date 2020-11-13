package by.aermakova.todoapp.data.useCase

import android.util.Log
import by.aermakova.todoapp.data.interactor.*
import by.aermakova.todoapp.data.remote.DeleteGoalItems
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DeleteGoalUseCase(
    private val goalInteractor: GoalInteractor,
    private val keyResultInteractor: KeyResultInteractor,
    private val stepInteractor: StepInteractor,
    private val taskInteractor: TaskInteractor,
    private val ideaInteractor: IdeaInteractor,
    private val errorMessage: String
) {

    private var goalId: Long = -1L
    private lateinit var errorAction: (String) -> Unit

    fun deleteGoalById(
        goalId: Long,
        disposable: CompositeDisposable,
        errorAction: (String) -> Unit
    ) {
        this.goalId = goalId
        this.errorAction = errorAction
        disposable.add(
            deleteGoalItemsFromRemote(keyResultInteractor) {
                deleteGoalItemsFromRemote(stepInteractor) {
                    deleteGoalItemsFromRemote(taskInteractor) {
                        deleteGoalItemsFromRemote(ideaInteractor) {
                            goalInteractor.deleteGoalByIdRemote(goalId)
                                .compose { goalInteractor.deleteGoalAndAllItsItemsLocal(goalId) }
                                .subscribe(
                                    { Log.d("A_DeleteGoalUseCase", "Goal and all its items hab been deleted") },
                                    {
                                        it.printStackTrace()
                                        errorAction.invoke(errorMessage)
                                    }
                                )
                        }
                    }
                }
            }
        )
    }

    private fun deleteGoalItemsFromRemote(
        deleteGoalItems: DeleteGoalItems,
        nextAction: () -> Unit
    ) = deleteGoalItems.deleteGoalsItemsById(goalId)
        .subscribeOn(Schedulers.io())
        .subscribe(
            { nextAction.invoke() },
            {
                it.printStackTrace()
                errorAction.invoke(errorMessage)
            }
        )
}
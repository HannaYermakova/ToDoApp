package by.aermakova.todoapp.data.useCase.delete

import android.util.Log
import by.aermakova.todoapp.data.interactor.*
import by.aermakova.todoapp.data.remote.DeleteGoalItems
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.util.handleError
import io.reactivex.schedulers.Schedulers

class DeleteGoalUseCase(
    private val goalInteractor: GoalInteractor,
    private val keyResultInteractor: KeyResultInteractor,
    private val stepInteractor: StepInteractor,
    private val taskInteractor: TaskInteractor,
    private val ideaInteractor: IdeaInteractor,
    private val errorMessage: String,
    dialogNavigation: DialogNavigation<Boolean>,
    dialogTitle: String
) : DeleteItemUseCase(dialogNavigation, dialogTitle) {


    override fun deleteById() {
        disposable.add(
            deleteGoalItemsFromRemote(keyResultInteractor) {
                deleteGoalItemsFromRemote(stepInteractor) {
                    deleteGoalItemsFromRemote(taskInteractor) {
                        deleteGoalItemsFromRemote(ideaInteractor) {
                            goalInteractor.deleteGoalByIdRemote(itemId)
                                .compose { goalInteractor.deleteGoalAndAllItsItemsLocal(itemId) }
                                .subscribe(
                                    { Log.d("A_DeleteGoalUseCase", "Goal and all its items hab been deleted") },
                                    {it.handleError(errorMessage, errorAction)}
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
    ) = deleteGoalItems.deleteGoalsItemsById(itemId)
        .subscribeOn(Schedulers.io())
        .subscribe(
            { nextAction.invoke() },
            {
                it.printStackTrace()
                errorAction.invoke(errorMessage)
            }
        )
}
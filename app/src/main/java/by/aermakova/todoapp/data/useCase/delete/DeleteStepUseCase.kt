package by.aermakova.todoapp.data.useCase.delete

import android.util.Log
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.data.model.FunctionNoArgs
import by.aermakova.todoapp.data.remote.DeleteStepItems
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.util.handleError
import io.reactivex.schedulers.Schedulers

class DeleteStepUseCase(
    private val goalInteractor: GoalInteractor,
    private val stepInteractor: StepInteractor,
    private val taskInteractor: TaskInteractor,
    private val ideaInteractor: IdeaInteractor,
    private val errorMessage: String,
    dialogNavigation: DialogNavigation<Boolean>,
    dialogTitle: String
) : DeleteItemUseCase(dialogNavigation, dialogTitle) {

    override fun deleteById() {
        disposable.add(
            deleteItemsFromRemote(taskInteractor) {
                deleteItemsFromRemote(ideaInteractor) {
                    stepInteractor.deleteStepByIdRemote(itemId)
                        .compose { goalInteractor.deleteStepAndAllItsItemsLocal(itemId) }
                        .subscribe(
                            { Log.d("A_DeleteStepUseCase", "Step has been deleted") },
                            { it.handleError(errorMessage, errorAction) }
                        )
                }
            }
        )
    }

    private fun deleteItemsFromRemote(
        deleteStepItems: DeleteStepItems,
        nextAction: FunctionNoArgs
    ) = deleteStepItems.deleteStepItemsById(itemId)
        .subscribeOn(Schedulers.io())
        .subscribe(
            { nextAction.invoke() },
            { it.handleError(errorMessage, errorAction) }
        )
}
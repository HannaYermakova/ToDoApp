package by.aermakova.todoapp.data.useCase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.data.remote.DeleteStepItems
import by.aermakova.todoapp.ui.goal.main.INIT_SELECTED_ITEM_ID
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.util.handleError
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DeleteStepUseCase(
    private val dialogNavigation: DialogNavigation<Boolean>,
    private val dialogTitle: String,
    private val goalInteractor: GoalInteractor,
    private val stepInteractor: StepInteractor,
    private val taskInteractor: TaskInteractor,
    private val ideaInteractor: IdeaInteractor,
    private val errorMessage: String
) {

    private var stepId: Long = INIT_SELECTED_ITEM_ID
    private lateinit var errorAction: (String) -> Unit
    private lateinit var disposable: CompositeDisposable

    private val _cancelAction = MutableLiveData<Boolean>(false)
    val cancelAction: LiveData<Boolean>
        get() = _cancelAction

    fun confirmDelete(value: Boolean?) {
        value?.let {
            if (it) {
                deleteById()
            } else {
                cancelAction()
            }
        } ?: cancelAction()
    }

    fun confirmDeleteStep(
        stepId: Long,
        disposable: CompositeDisposable,
        errorAction: (String) -> Unit
    ) {
        this.stepId = stepId
        this.errorAction = errorAction
        this.disposable = disposable
        dialogNavigation.openItemDialog(dialogTitle)
    }

    val logoutObserver: LiveData<Boolean>?
        get() = dialogNavigation.getDialogResult()

    private fun deleteById() {
        disposable.add(
            deleteItemsFromRemote(taskInteractor) {
                deleteItemsFromRemote(ideaInteractor) {
                    stepInteractor.deleteStepByIdRemote(stepId)
                        .compose { goalInteractor.deleteStepAndAllItsItemsLocal(stepId) }
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

    private fun cancelAction() {
        Log.d("A_DeleteStepUseCase", "cancel action")
        _cancelAction.postValue(true)
    }
}
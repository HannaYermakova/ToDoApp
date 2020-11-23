package by.aermakova.todoapp.data.useCase.delete

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.model.FunctionString
import by.aermakova.todoapp.ui.goal.main.INIT_SELECTED_ITEM_ID
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import io.reactivex.disposables.CompositeDisposable

abstract class DeleteItemUseCase(
    private val dialogNavigation: DialogNavigation<Boolean>,
    private val dialogTitle: String
) {

    protected var itemId: Long = INIT_SELECTED_ITEM_ID
    protected lateinit var disposable: CompositeDisposable
    protected lateinit var errorAction: FunctionString

    private val _cancelAction = MutableLiveData<Boolean>(false)
    val cancelAction: LiveData<Boolean>
        get() = _cancelAction

    fun confirmDelete(value: Boolean?) {
        value?.let {
            if (it) {
                deleteById()
                dialogNavigation.setDialogResult(false)
            } else {
                cancelAction()
            }
        } ?: cancelAction()
    }

    fun confirmDeleteItem(
        id: Long,
        disposable: CompositeDisposable,
        errorAction: FunctionString
    ) {
        this.itemId = id
        this.errorAction = errorAction
        this.disposable = disposable
        dialogNavigation.openItemDialog(dialogTitle)
    }

    val deleteConfirmObserver: LiveData<Boolean>?
        get() = dialogNavigation.getDialogResult()

    abstract fun deleteById()

    private fun cancelAction() {
        _cancelAction.postValue(true)
    }
}
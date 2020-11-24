package by.aermakova.todoapp.data.useCase.bottomMenu

import android.content.res.Resources
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.model.FunctionString
import by.aermakova.todoapp.data.useCase.delete.DeleteItemUseCase
import by.aermakova.todoapp.data.useCase.actionEnum.ActionTextConverter
import by.aermakova.todoapp.data.useCase.actionEnum.getLiveListOfActionsItems
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.goal.main.INIT_SELECTED_ITEM_ID
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.disposables.CompositeDisposable


abstract class CommonBottomSheetMenuUseCase<
        VM : BaseViewModel,
        Entity,
        DeleteUseCase : DeleteItemUseCase,
        ActionItem : ActionTextConverter>(
    val deleteItemUseCase: DeleteUseCase,
    private val dialog: BottomSheetDialog,
    private val ideaActionItems: Array<ActionItem>,
    private val resources: Resources,
) {

    protected var selectedItemId = INIT_SELECTED_ITEM_ID

    private val _liveListOfItemsActionsItems = MutableLiveData<List<CommonModel>>()
    val liveListOfItemsActionsItems: LiveData<List<CommonModel>>
        get() = _liveListOfItemsActionsItems

    fun openBottomSheetActions(
        disposable: CompositeDisposable,
        id: Long,
        viewModel: VM,
        errorAction: FunctionString
    ) {
        bindViewModel(viewModel)
        selectedItemId = id
        checkIsTaskDone(disposable, selectedItemId, errorAction)
    }

    abstract fun bindViewModel(viewModel: VM)
    abstract val rootView: View

    private fun checkIsTaskDone(
        disposable: CompositeDisposable,
        itemId: Long,
        errorAction: FunctionString
    ) {
        useItemById(
            disposable, itemId, {
                getLiveListOfStepActionsItems(disposable, checkIsItemDone(it), errorAction)
                dialog.setContentView(rootView)
                dialog.show()
            },
            errorAction
        )
    }

    abstract fun useItemById(
        disposable: CompositeDisposable,
        itemId: Long,
        function: (Entity) -> Unit,
        errorAction: FunctionString
    )

    abstract fun checkIsItemDone(entity: Entity): Boolean

    private fun getLiveListOfStepActionsItems(
        disposable: CompositeDisposable,
        taskIsDone: Boolean,
        errorAction: FunctionString
    ) {
        _liveListOfItemsActionsItems.postValue(
            ideaActionItems.getLiveListOfActionsItems(
                disposable,
                errorAction,
                resources,
                taskIsDone,
                { item, disp, error -> setAction(item, disp, error) })
        )
    }

    private fun setAction(
        action: ActionItem,
        disposable: CompositeDisposable,
        errorAction: FunctionString
    ) {
        dialog.dismiss()
        setItemAction(action, disposable, errorAction)
        selectedItemId = INIT_SELECTED_ITEM_ID
    }

    abstract fun setItemAction(
        action: ActionItem,
        disposable: CompositeDisposable,
        errorAction: FunctionString
    )
}
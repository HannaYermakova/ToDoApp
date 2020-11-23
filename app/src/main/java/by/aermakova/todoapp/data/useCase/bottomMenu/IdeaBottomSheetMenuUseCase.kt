package by.aermakova.todoapp.data.useCase.bottomMenu

import android.content.res.Resources
import android.view.View
import by.aermakova.todoapp.data.db.entity.IdeaEntity
import by.aermakova.todoapp.data.useCase.DeleteIdeaUseCase
import by.aermakova.todoapp.data.useCase.FindIdeaUseCase
import by.aermakova.todoapp.data.useCase.actionEnum.IdeasActionItem
import by.aermakova.todoapp.databinding.BottomSheetIdeaActionBinding
import by.aermakova.todoapp.ui.idea.main.IdeaViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.disposables.CompositeDisposable


class IdeaBottomSheetMenuUseCase(
    val deleteIdeaUseCase: DeleteIdeaUseCase,
    private val ideaActionBind: BottomSheetIdeaActionBinding,
    private val mainFlowNavigation: MainFlowNavigation,
    private val findIdeaUseCase: FindIdeaUseCase,
    dialog: BottomSheetDialog,
    ideaActionItems: Array<IdeasActionItem>,
    resources: Resources
) : CommonBottomSheetMenuUseCase<IdeaViewModel, IdeaEntity, DeleteIdeaUseCase, IdeasActionItem>(
    deleteIdeaUseCase, dialog, ideaActionItems, resources
) {

    override fun bindViewModel(viewModel: IdeaViewModel) {
        ideaActionBind.viewModel = viewModel
    }

    override val rootView: View
        get() = ideaActionBind.root

    override fun useItemById(
        disposable: CompositeDisposable,
        itemId: Long,
        function: (IdeaEntity) -> Unit,
        errorAction: (String) -> Unit
    ) {
        findIdeaUseCase.useIdeasById(
            disposable, itemId, function, errorAction
        )
    }

    override fun checkIsItemDone(entity: IdeaEntity) = false

    override fun setItemAction(
        action: IdeasActionItem,
        disposable: CompositeDisposable,
        errorAction: (String) -> Unit
    ) {
        when (action) {
            IdeasActionItem.EDIT_IDEA -> mainFlowNavigation.navigateToEditElementFragment(
                selectedItemId
            )
            IdeasActionItem.DELETE_IDEA -> deleteIdeaUseCase.confirmDeleteItem(
                selectedItemId,
                disposable,
                errorAction
            )
        }
    }
}
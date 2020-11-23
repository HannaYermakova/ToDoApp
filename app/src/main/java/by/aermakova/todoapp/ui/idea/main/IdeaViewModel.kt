package by.aermakova.todoapp.ui.idea.main

import androidx.lifecycle.LiveData
import by.aermakova.todoapp.data.di.scope.NavigationIdeas
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.model.FunctionLong
import by.aermakova.todoapp.data.useCase.bottomMenu.IdeaBottomSheetMenuUseCase
import by.aermakova.todoapp.data.useCase.LoadAllIdeasUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Item
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class IdeaViewModel @Inject constructor(
    @NavigationIdeas private val navigation: MainFlowNavigation,
    private val ideaBottomSheetMenuUseCase: IdeaBottomSheetMenuUseCase,
    loadAllIdeasUseCase: LoadAllIdeasUseCase
) : BaseViewModel() {

    override val mainFlowNavigation: MainFlowNavigation
        get() = navigation

    private val _ideasList = PublishSubject.create<List<CommonModel>>()
    val ideasList: Observable<List<CommonModel>>
        get() = _ideasList.hide()

    val addNewElement = { mainFlowNavigation.navigateToAddNewElementFragment(item = Item.GOAL) }

    val actionItems: LiveData<List<CommonModel>> =
        ideaBottomSheetMenuUseCase.liveListOfItemsActionsItems

    val confirmDeleteListener = ideaBottomSheetMenuUseCase.deleteIdeaUseCase.deleteConfirmObserver

    val cancelAction = ideaBottomSheetMenuUseCase.deleteIdeaUseCase.cancelAction

    fun confirmDelete(value: Boolean?) {
        ideaBottomSheetMenuUseCase.deleteIdeaUseCase.confirmDelete(value)
    }

    val deleteAction: FunctionLong = {
        ideaBottomSheetMenuUseCase.deleteIdeaUseCase.confirmDeleteItem(
            it,
            disposable,
            errorAction
        )
    }

    private val openBottomSheetActions: FunctionLong = {
        ideaBottomSheetMenuUseCase.openBottomSheetActions(disposable, it, this, errorAction)
    }

    init {
        loadingAction.invoke()
        loadAllIdeasUseCase.loadIdeas(
            disposable,
            { navigation.navigateToShowDetailsFragment(it) },
            openBottomSheetActions,
            {
                successAction.invoke()
                _ideasList.onNext(it)
            },
            errorAction
        )
    }
}
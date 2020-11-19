package by.aermakova.todoapp.ui.idea.main

import by.aermakova.todoapp.data.di.scope.NavigationIdeas
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.useCase.LoadAllIdeasUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Item
import by.aermakova.todoapp.util.Status
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class IdeaViewModel @Inject constructor(
    @NavigationIdeas private val navigation: MainFlowNavigation,
    loadAllIdeasUseCase: LoadAllIdeasUseCase
) : BaseViewModel() {

    override val mainFlowNavigation: MainFlowNavigation
        get() = navigation

    private val _ideasList = PublishSubject.create<List<CommonModel>>()
    val ideasList: Observable<List<CommonModel>>
        get() = _ideasList.hide()

    val addNewElement = { mainFlowNavigation.navigateToAddNewElementFragment(item = Item.GOAL) }

    init {
        loadingAction.invoke()
        loadAllIdeasUseCase.loadIdeas(
            disposable,
            { mainFlowNavigation.navigateToShowDetailsFragment(it) },
            {
                successAction.invoke()
                _ideasList.onNext(it)
            },
            errorAction
        )
    }
}
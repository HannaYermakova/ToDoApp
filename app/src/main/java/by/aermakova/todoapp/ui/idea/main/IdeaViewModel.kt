package by.aermakova.todoapp.ui.idea.main

import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.useCase.LoadAllIdeasUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Status
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class IdeaViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    loadAllIdeasUseCase: LoadAllIdeasUseCase
) : BaseViewModel() {

    private val _ideasList = PublishSubject.create<List<CommonModel>>()
    val ideasList: Observable<List<CommonModel>>
        get() = _ideasList.hide()

    val addNewElement = { mainFlowNavigation.navigateToAddNewElementFragment() }

    init {
        _status.onNext(Status.LOADING)
        loadAllIdeasUseCase.loadIdeas(
            disposable,
            { mainFlowNavigation.navigateToShowDetailsFragment(it) },
            {
                _status.onNext(Status.SUCCESS)
                _ideasList.onNext(it)
            },
            { _status.onNext(Status.ERROR.apply { message = it }) }
        )
    }
}
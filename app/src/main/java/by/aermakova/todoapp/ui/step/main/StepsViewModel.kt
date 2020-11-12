package by.aermakova.todoapp.ui.step.main

import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.useCase.LoadAllStepsUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Status
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class StepsViewModel @Inject constructor(
    private val navigation: MainFlowNavigation,
    loadAllStepsUseCase: LoadAllStepsUseCase
) : BaseViewModel() {

    private val _stepsList = PublishSubject.create<List<CommonModel>>()
    val stepsList: Observable<List<CommonModel>>
        get() = _stepsList.hide()

    val addNewElement = { navigation.navigateToAddNewElementFragment() }

    init {
        loadAllStepsUseCase.loadSteps(
            disposable,
            { navigation.navigateToShowDetailsFragment(it) },
            {
                _status.onNext(Status.SUCCESS)
                _stepsList.onNext(it)
            },
            errorAction
        )
    }
}
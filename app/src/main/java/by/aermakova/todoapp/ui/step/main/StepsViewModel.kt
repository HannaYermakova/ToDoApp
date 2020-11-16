package by.aermakova.todoapp.ui.step.main

import androidx.lifecycle.LiveData
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.useCase.LoadAllStepsUseCase
import by.aermakova.todoapp.data.useCase.StepBottomSheetMenuUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Status
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class StepsViewModel @Inject constructor(
    private val navigation: MainFlowNavigation,
    private val stepBottomSheetMenuUseCase: StepBottomSheetMenuUseCase,
    loadAllStepsUseCase: LoadAllStepsUseCase
) : BaseViewModel() {

    val actionItems: LiveData<List<CommonModel>> =
        stepBottomSheetMenuUseCase.getLiveListOfStepActionsItems(disposable, errorAction)

    private val openBottomSheetActions: (Long) -> Unit = {
        stepBottomSheetMenuUseCase.openBottomSheetActions(it, this)
    }

    private val _stepsList = PublishSubject.create<List<CommonModel>>()
    val stepsList: Observable<List<CommonModel>>
        get() = _stepsList.hide()

    val addNewElement = { navigation.navigateToAddNewElementFragment() }

    init {
        loadAllStepsUseCase.loadSteps(
            disposable,
            { navigation.navigateToShowDetailsFragment(it) },
            openBottomSheetActions,
            {
                _status.onNext(Status.SUCCESS)
                _stepsList.onNext(it)
            },
            errorAction
        )
    }
}
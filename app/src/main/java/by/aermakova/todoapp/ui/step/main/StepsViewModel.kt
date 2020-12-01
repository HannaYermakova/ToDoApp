package by.aermakova.todoapp.ui.step.main

import androidx.lifecycle.LiveData
import by.aermakova.todoapp.data.di.scope.NavigationSteps
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.model.FunctionLong
import by.aermakova.todoapp.data.useCase.LoadAllStepsUseCase
import by.aermakova.todoapp.data.useCase.bottomMenu.StepBottomSheetMenuUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.step.StepsNavigation
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class StepsViewModel @Inject constructor(
    @NavigationSteps private val navigation: StepsNavigation,
    private val stepBottomSheetMenuUseCase: StepBottomSheetMenuUseCase,
    loadAllStepsUseCase: LoadAllStepsUseCase
) : BaseViewModel() {

    override val mainFlowNavigation: MainFlowNavigation
        get() = navigation

    val actionItems: LiveData<List<CommonModel>> =
        stepBottomSheetMenuUseCase.liveListOfItemsActionsItems

    private val openBottomSheetActions: FunctionLong = {
        stepBottomSheetMenuUseCase.openBottomSheetActions(disposable, it, this, errorAction)
    }
    private val _stepsList = PublishSubject.create<List<CommonModel>>()

    val stepsList: Observable<List<CommonModel>>
        get() = _stepsList.hide()

    val deleteAction: FunctionLong = {
        stepBottomSheetMenuUseCase.deleteItemUseCase.confirmDeleteItem(
            it,
            disposable,
            errorAction
        )
    }

    val addNewElement = { navigation.navigateToAddNewElementFragment() }

    val confirmDeleteListener = stepBottomSheetMenuUseCase.deleteItemUseCase.deleteConfirmObserver

    val cancelAction = stepBottomSheetMenuUseCase.deleteItemUseCase.cancelAction

    fun confirmDelete(value: Boolean?) {
        stepBottomSheetMenuUseCase.deleteItemUseCase.confirmDelete(value)
    }

    init {
        loadAllStepsUseCase.loadSteps(
            disposable,
            { navigation.navigateToShowDetailsFragment(it) },
            openBottomSheetActions,
            {
                successAction.invoke()
                _stepsList.onNext(it)
            },
            errorAction
        )
    }
}
package by.aermakova.todoapp.ui.goal.edit

import by.aermakova.todoapp.data.useCase.AddNewKeyResultsToGoalUseCase
import by.aermakova.todoapp.data.useCase.ChangeGoalTextUseCase
import by.aermakova.todoapp.data.useCase.FindGoalUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import javax.inject.Inject

class EditGoalViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    val changeGoalTextUseCase: ChangeGoalTextUseCase,
    val addNewKeyResultsToGoalUseCase: AddNewKeyResultsToGoalUseCase,
    findGoalUseCase: FindGoalUseCase,
    goalId: Long
) : BaseViewModel() {

    val popBack = { mainFlowNavigation.popBack() }

    val saveChanges = {
        changeGoalTextUseCase.saveChanges(goalId, disposable, errorAction)
        addNewKeyResultsToGoalUseCase.saveChanges(disposable, errorAction)
    }


    init {
        findGoalUseCase.getGoalKeyResultsById(
            goalId, disposable, errorAction
        ) {
            changeGoalTextUseCase.setExistingGoalTitle(it.goal.text)
            addNewKeyResultsToGoalUseCase.initKeyResultList(
                goalId,
                it.keyResults.map { keyRes -> keyRes.text },
                disposable,
                errorAction
            )
        }
    }
}
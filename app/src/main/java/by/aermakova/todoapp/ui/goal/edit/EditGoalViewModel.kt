package by.aermakova.todoapp.ui.goal.edit

import by.aermakova.todoapp.data.useCase.AddNewKeyResultsToGoalUseCase
import by.aermakova.todoapp.data.useCase.ChangeGoalTextUseCase
import by.aermakova.todoapp.data.useCase.FindGoalUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class EditGoalViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    val changeGoalTextUseCase: ChangeGoalTextUseCase,
    val addNewKeyResultsToGoalUseCase: AddNewKeyResultsToGoalUseCase,
    findGoalUseCase: FindGoalUseCase,
    goalId: Long
) : BaseViewModel() {

    val popBack = { mainFlowNavigation.popBack() }

    private val _saveGoalTextSuccess = BehaviorSubject.create<Boolean>()
    private val _saveNewKeyResultsToSuccess = BehaviorSubject.create<Boolean>()

    val saveChanges = {
        changeGoalTextUseCase.saveChanges(goalId, disposable, _saveGoalTextSuccess, errorAction)
        addNewKeyResultsToGoalUseCase.saveChanges(
            disposable,
            _saveNewKeyResultsToSuccess,
            errorAction
        )
    }


    init {
        disposable.add(
            _saveGoalTextSuccess
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { saveGoalTextSuccess ->
                        _saveNewKeyResultsToSuccess.observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                {
                                    if (saveGoalTextSuccess || it) {
                                        mainFlowNavigation.popBack()
                                        disposable.dispose()
                                    }
                                },
                                { it.printStackTrace() }
                            )
                    },
                    { it.printStackTrace() }
                )
        )

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
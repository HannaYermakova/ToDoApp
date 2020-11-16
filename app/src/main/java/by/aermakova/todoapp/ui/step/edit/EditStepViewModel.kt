package by.aermakova.todoapp.ui.step.edit

import by.aermakova.todoapp.data.useCase.ChangeStepTextUseCase
import by.aermakova.todoapp.data.useCase.FindStepUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class EditStepViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    val changeStepTextUseCase: ChangeStepTextUseCase,
    findStepUseCase: FindStepUseCase,
    stepId: Long
) : BaseViewModel() {

    private val _saveStepTextSuccess = BehaviorSubject.create<Boolean>()
    val popBack = { mainFlowNavigation.popBack() }
    val saveStep = {
        changeStepTextUseCase.saveChanges(stepId, disposable, _saveStepTextSuccess, errorAction)
    }

    init {
        disposable.add(
            _saveStepTextSuccess
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { if (it) mainFlowNavigation.popBack() },
                    { it.printStackTrace() }
                )
        )
        findStepUseCase.findStepById(stepId, {
            changeStepTextUseCase.setExistingStepTitle(it.text)
        }, errorAction)
    }
}
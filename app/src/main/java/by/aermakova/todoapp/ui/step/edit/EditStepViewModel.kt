package by.aermakova.todoapp.ui.step.edit

import by.aermakova.todoapp.data.db.entity.StepEntity
import by.aermakova.todoapp.data.di.scope.NavigationSteps
import by.aermakova.todoapp.data.useCase.FindStepUseCase
import by.aermakova.todoapp.data.useCase.editText.ChangeItemTextUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.ui.step.StepsNavigation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class EditStepViewModel @Inject constructor(
    @NavigationSteps private val navigation: StepsNavigation,
    val changeStepTextUseCase: ChangeItemTextUseCase<StepEntity>,
    findStepUseCase: FindStepUseCase,
    stepId: Long
) : BaseViewModel() {

    override val mainFlowNavigation: MainFlowNavigation
        get() = navigation

    private val _saveStepTextSuccess = BehaviorSubject.create<Boolean>()

    val saveStep = {
        changeStepTextUseCase.saveChanges(stepId, disposable, _saveStepTextSuccess, errorAction)
    }

    init {
        disposable.add(
            _saveStepTextSuccess
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { if (it) popBack.invoke() },
                    { it.printStackTrace() }
                )
        )
        findStepUseCase.findStepById(stepId, {
            changeStepTextUseCase.setExistingItemText(it.text)
        }, errorAction)
    }
}
package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.db.entity.StepEntity
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.ui.goal.main.INIT_SELECTED_ITEM_ID
import by.aermakova.todoapp.util.handleError
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FindStepUseCase(
    private val stepInteractor: StepInteractor,
    private val errorMessage: String
) {

    fun useStepByIdInUiThread(
        stepId: Long?,
        successAction: (StepEntity) -> Unit,
        errorAction: (String) -> Unit
    ): Disposable? {
        return stepId?.let {
            if (stepId > INIT_SELECTED_ITEM_ID){
            stepInteractor.getStepById(stepId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { step -> successAction.invoke(step) },
                    { it.handleError(errorMessage, errorAction) }
                )
            } else null
        }
    }

    fun findStepById(
        stepId: Long?,
        successAction: (StepEntity) -> Unit,
        errorAction: (String) -> Unit
    ): Disposable? {
        return stepId?.let {
            stepInteractor.getStepById(stepId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { step -> successAction.invoke(step) },
                    { it.handleError(errorMessage, errorAction) }
                )
        }
    }
}
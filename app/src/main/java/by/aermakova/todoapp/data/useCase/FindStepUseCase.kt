package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.db.entity.StepEntity
import by.aermakova.todoapp.data.interactor.StepInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class FindStepUseCase(
    private val stepInteractor: StepInteractor
) {

    fun useStepById(
        stepId: Long?,
        successAction: (StepEntity) -> Unit,
        errorAction: ((String?) -> Unit)? = null
    ): Disposable? {
        return stepId?.let {
            stepInteractor.getStepById(stepId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { step -> successAction.invoke(step) },
                    {
                        it.printStackTrace()
                        errorAction?.invoke(it.message)
                    }
                )
        }
    }
}
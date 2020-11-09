package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.util.ITEM_IS_NOT_SELECTED_ID
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CreateStepUseCase(
    private val stepInteractor: StepInteractor,
    private val errorMessage: String,
) {

    fun saveStepToLocalDataBaseAndSyncToRemote(
        disposable: CompositeDisposable,
        tempStepTitle: String?,
        tempGoalId: Long?,
        tempKeyResultId: Long?,
        successAction: () -> Unit,
        errorAction: ((String) -> Unit)?
    ) {
        if (!tempStepTitle.isNullOrBlank()
            && tempGoalId != null && tempGoalId > ITEM_IS_NOT_SELECTED_ID
            && tempKeyResultId != null && tempKeyResultId > ITEM_IS_NOT_SELECTED_ID
        ) {
            disposable.add(
                stepInteractor.createStep(
                    tempStepTitle,
                    tempGoalId,
                    tempKeyResultId
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { successAction.invoke() },
                        {
                            errorAction?.invoke(errorMessage)
                            it.printStackTrace()
                        }
                    )
            )
        } else {
            errorAction?.invoke(errorMessage)
        }
    }
}
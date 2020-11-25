package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.model.FunctionNoArgs
import by.aermakova.todoapp.data.model.FunctionString
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CreateGoalUseCase(
    private val goalInteractor: GoalInteractor,
    private val errorMessage: String
) {

    fun saveGoalToLocalDataBaseAndSyncToRemote(
        disposable: CompositeDisposable,
        tempGoalTitle: String?,
        tempKeyResults: List<String>,
        successAction: FunctionNoArgs,
        errorAction: FunctionString? = null
    ) {
        if (!tempGoalTitle.isNullOrBlank()
            && tempKeyResults.isNotEmpty()
        ) {
            disposable.add(
                Single.create<Long> {
                    it.onSuccess(
                        goalInteractor.saveGoalAndKeyResToLocal(
                            tempGoalTitle,
                            tempKeyResults
                        )
                    )
                }
                    .map {
                        goalInteractor.getGoalKeyResultsById(it).firstElement()
                            .subscribe { goalKeyResults ->
                                goalInteractor.saveGoalAndKeyResultsToRemote(goalKeyResults)
                            }
                    }
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
        } else errorAction?.invoke(errorMessage)
    }
}
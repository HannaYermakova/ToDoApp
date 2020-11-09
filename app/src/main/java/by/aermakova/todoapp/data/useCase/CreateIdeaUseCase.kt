package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.util.ITEM_IS_NOT_SELECTED_ID
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CreateIdeaUseCase(
    private val ideaInteractor: IdeaInteractor,
    private val errorMessage: String
) {

    fun saveIdeaToLocalDataBaseAndSyncToRemote(
        disposable: CompositeDisposable,
        tempIdeaTitle: String?,
        tempGoalId: Long?,
        tempKeyResultId: Long?,
        tempStepId: Long?,
        successAction: () -> Unit,
        errorAction: ((String) -> Unit)? = null
    ) {
        if (!tempIdeaTitle.isNullOrBlank()
            && tempGoalId != null
            && tempGoalId > ITEM_IS_NOT_SELECTED_ID
        ) {
            disposable.add(
                Single.create<Long> {
                    it.onSuccess(
                        ideaInteractor
                            .saveIdeaInLocalDatabase(
                                tempIdeaTitle,
                                tempGoalId,
                                tempKeyResultId,
                                tempStepId
                            )
                    )
                }.map {
                    ideaInteractor.getIdeaById(it)
                        .subscribe { entity ->
                            ideaInteractor.saveIdeaToRemote(entity)
                        }
                }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { successAction.hashCode() },
                        {
                            errorAction?.invoke(errorMessage)
                            it.printStackTrace()
                        }
                    )
            )
        } else errorAction?.invoke(errorMessage)
    }
}
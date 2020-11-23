package by.aermakova.todoapp.data.useCase.delete

import android.util.Log
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.util.handleError
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DeleteIdeaUseCase(
    private val ideaInteractor: IdeaInteractor,
    private val errorMessage: String,
    dialogNavigation: DialogNavigation<Boolean>,
    dialogTitle: String
) : DeleteItemUseCase(dialogNavigation, dialogTitle) {


    override fun deleteById() {
        disposable.add(
            ideaInteractor.deleteIdeaByIdRemote(itemId)
                .subscribeOn(Schedulers.io())
                .doOnSuccess { ideaInteractor.deleteIdeaByIdLocal(itemId) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { Log.d("A_DeleteIdeaUseCase", "Idea has been deleted") },
                    { it.handleError(errorMessage, errorAction) }
                )
        )
    }
}
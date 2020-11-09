package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.model.toCommonModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoadAllIdeasUseCase(
    private val ideaInteractor: IdeaInteractor,
    private val errorMessage: String
) {

    fun loadIdeas(
        disposable: CompositeDisposable,
        selectAction: (Long) -> Unit,
        successAction: (List<CommonModel>) -> Unit,
        errorAction: ((String) -> Unit)? = null

    ) {
        disposable.add(
            ideaInteractor.getAllIdeas().observeList(
                errorMessage,
                { it.toCommonModel(selectAction) },
                successAction, errorAction
            )
        )
    }
}

fun <Model> Observable<List<Model>>.observeList(
    errorMessage: String,
    mapEntityToCommonModel: (Model) -> CommonModel,
    successAction: (List<CommonModel>) -> Unit,
    errorAction: ((String) -> Unit)? = null
): Disposable {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { list ->
                list.map { mapEntityToCommonModel(it) }.let {
                    successAction.invoke(it)
                }
            },
            {
                errorAction?.invoke(errorMessage)
                it.printStackTrace()
            }
        )
}
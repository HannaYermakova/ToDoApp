package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.model.FunctionLong
import by.aermakova.todoapp.data.model.FunctionString
import by.aermakova.todoapp.data.model.toCommonModel
import by.aermakova.todoapp.util.handleError
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
        selectAction: FunctionLong,
        longClickAction: FunctionLong,
        successAction: (List<CommonModel>) -> Unit,
        errorAction: FunctionString? = null
    ) {
        disposable.add(
            ideaInteractor.getAllIdeas().observeList(
                errorMessage,
                { it.toCommonModel(selectAction, longClickAction) },
                successAction, errorAction
            )
        )
    }
}

fun <Model> Observable<List<Model>>.observeList(
    errorMessage: String,
    mapEntityToCommonModel: (Model) -> CommonModel,
    successAction: (List<CommonModel>) -> Unit,
    errorAction: FunctionString? = null
): Disposable {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { list ->
                list.map { mapEntityToCommonModel(it) }.let {
                    successAction.invoke(it)
                }
            },
            { it.handleError(errorMessage, errorAction) }
        )
}
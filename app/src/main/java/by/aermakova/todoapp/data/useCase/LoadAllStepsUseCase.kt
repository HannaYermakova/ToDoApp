package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.model.FunctionLong
import by.aermakova.todoapp.data.model.FunctionString
import by.aermakova.todoapp.data.model.toCommonModel
import by.aermakova.todoapp.util.handleError
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoadAllStepsUseCase(
    private val stepInteractor: StepInteractor,
    private val errorMessage: String
) {

    fun loadSteps(
        disposable: CompositeDisposable,
        selectAction: FunctionLong,
        longClickAction: FunctionLong? = null,
        successAction: (List<CommonModel>) -> Unit,
        errorAction: FunctionString? = null
    ) {
        disposable.add(
            stepInteractor.getAllSteps()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { list ->
                        list.map {
                            it.toCommonModel(
                                clickAction = { id -> selectAction.invoke(id) },
                                longAction = { id -> longClickAction?.invoke(id) }
                            )
                        }.let {
                            successAction.invoke(it)
                        }
                    },
                    { it.handleError(errorMessage, errorAction) }
                )
        )
    }
}
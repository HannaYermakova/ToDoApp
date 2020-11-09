package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.model.toCommonModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoadAllStepsUseCase(
    private val stepInteractor: StepInteractor,
    private val errorMessage: String
) {

    fun loadSteps(
        disposable: CompositeDisposable,
        selectAction: (Long) -> Unit,
        successAction: (List<CommonModel>) -> Unit,
        errorAction: ((String) -> Unit)? = null
    ) {
        disposable.add(
            stepInteractor.getAllSteps()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { list ->
                        list.map {
                            it.toCommonModel { id ->
                                selectAction.invoke(id)
                            }
                        }.let {
                            successAction.invoke(it)
                        }
                    },
                    {
                        errorAction?.invoke(errorMessage)
                        it.printStackTrace()
                    }
                )
        )
    }
}
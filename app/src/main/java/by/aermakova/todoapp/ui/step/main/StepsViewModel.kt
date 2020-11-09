package by.aermakova.todoapp.ui.step.main

import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.model.toCommonModel
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Status
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class StepsViewModel @Inject constructor(
    private val navigation: MainFlowNavigation,
    private val stepInteractor: StepInteractor
) : BaseViewModel() {

    private val _stepsList = PublishSubject.create<List<CommonModel>>()
    val stepsList: Observable<List<CommonModel>>
        get() = _stepsList.hide()

    val addNewElement = { navigation.navigateToAddNewElementFragment() }

    init {
        disposable.add(
            stepInteractor.getAllSteps()
                .map { list ->
                    list.map {
                        it.toCommonModel { id ->
                            navigation.navigateToShowDetailsFragment(
                                id
                            )
                        }
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _status.onNext(Status.SUCCESS)
                        _stepsList.onNext(it)
                    },
                    {
                        _status.onNext(Status.ERROR)
                        it.printStackTrace()
                    }
                )
        )
    }
}
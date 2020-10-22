package by.aermakova.todoapp.ui.step.main

import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.ui.adapter.CommonModel
import by.aermakova.todoapp.ui.adapter.toCommonModel
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
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
                .map { list -> list.map { it.toCommonModel { } } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _stepsList.onNext(it) },
                    { it.printStackTrace() }
                )
        )
    }
}
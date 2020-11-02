package by.aermakova.todoapp.ui.goal.main

import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.ui.adapter.CommonModel
import by.aermakova.todoapp.ui.adapter.toCommonModelGoalList
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Status
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class GoalsViewModel @Inject constructor(
    private val navigation: MainFlowNavigation,
    private val goalInteractor: GoalInteractor
) : BaseViewModel() {

    val addNewElement = { navigation.navigateToAddNewElementFragment() }

    private val _goalsList = PublishSubject.create<List<CommonModel>>()
    val goalsList: Observable<List<CommonModel>>
        get() = _goalsList.hide()

    init {
        _status.onNext(Status.LOADING)
        compositeDisposable.add(
            goalInteractor.getAllGoalsWithKeyResults()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _goalsList.onNext(it.toCommonModelGoalList { id ->
                            navigation.navigateToShowDetailsFragment(id)
                        })
                        _status.onNext(Status.SUCCESS)
                    },
                    {
                        it.printStackTrace()
                        _status.onNext(Status.ERROR)
                    }
                )
        )
    }
}
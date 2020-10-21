package by.aermakova.todoapp.ui.goal.main

import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.remote.model.GoalRemoteModel
import by.aermakova.todoapp.data.remote.model.KeyResultRemoteModel
import by.aermakova.todoapp.ui.adapter.CommonModel
import by.aermakova.todoapp.ui.adapter.toCommonModelGoalList
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
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
        syncGoalsRemoteDataBase()
        syncKeyResultsRemoteDataBase()
        compositeDisposable.add(
            goalInteractor.getAllGoalsWithKeyResults()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _goalsList.onNext(it.toCommonModelGoalList { id ->
                            navigation.navigateToShowDetailsFragment(
                                id
                            )
                        })
                    },
                    { it.printStackTrace() }
                )
        )
    }

    private fun syncGoalsRemoteDataBase() {
        val dataObserver = PublishSubject.create<List<GoalRemoteModel>>()
        goalInteractor.addGoalsDataListener(dataObserver)
        compositeDisposable.add(
            dataObserver
                .observeOn(Schedulers.io())
                .subscribe(
                    { goalInteractor.saveGoalsInLocalDatabase(it) },
                    { it.printStackTrace() }
                )
        )
    }

    private fun syncKeyResultsRemoteDataBase() {
        val dataObserver = PublishSubject.create<List<KeyResultRemoteModel>>()
        goalInteractor.addKeyResultsDataListener(dataObserver)
        compositeDisposable.add(
            dataObserver
                .observeOn(Schedulers.io())
                .subscribe(
                    { goalInteractor.saveKeyResultsInLocalDatabase(it) },
                    { it.printStackTrace() }
                )
        )
    }
}
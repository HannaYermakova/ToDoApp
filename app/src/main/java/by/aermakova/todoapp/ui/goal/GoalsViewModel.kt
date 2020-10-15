package by.aermakova.todoapp.ui.goal

import by.aermakova.todoapp.BR
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.model.Goal
import by.aermakova.todoapp.ui.adapter.Model
import by.aermakova.todoapp.ui.adapter.toModel
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

    private val _goalsList = PublishSubject.create<List<Model<Goal>>>()
    val goalsList: Observable<List<Model<Goal>>>
        get() = _goalsList.hide()

    init {
        _disposable.add(
            goalInteractor.getAllGoalsWithKeyResults()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _goalsList.onNext(it.toModelGoalList()) },
                    { it.printStackTrace() }
                )
        )
    }
}

fun List<Goal>.toModelGoalList(): List<Model<Goal>> {
    return map { toModel(it.goalId, it, R.layout.item_goal, BR.goalModel) }
}
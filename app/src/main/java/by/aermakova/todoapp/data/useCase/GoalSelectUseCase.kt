package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.model.TextModel
import by.aermakova.todoapp.data.model.toTextModel
import by.aermakova.todoapp.ui.goal.main.INIT_SELECTED_GOAL_ID
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class GoalSelectUseCase(
    private val goalInteractor: GoalInteractor
) {

    private val _goalsList = BehaviorSubject.create<List<TextModel>>()
    val goalsList: Observable<List<TextModel>>
        get() = _goalsList

    private val _selectedGoal = BehaviorSubject.create<TextModel>()
    val selectedGoal: Observable<TextModel>
        get() = _selectedGoal


    fun addCreationOfGoalListToDisposable(
        disposable: CompositeDisposable,
        selectedGoalId: Long = INIT_SELECTED_GOAL_ID
    ) {
        disposable.add(
            goalInteractor.getAllUndoneGoals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _goalsList.onNext(it.map { item ->
                            if (selectedGoalId > INIT_SELECTED_GOAL_ID) {
                                if (item.goalId == selectedGoalId) {
                                    _selectedGoal.onNext(item.toTextModel())
                                }
                            }
                            item.toTextModel()
                        })
                    },
                    { it.printStackTrace() }
                )
        )
    }
}
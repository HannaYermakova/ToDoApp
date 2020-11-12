package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AddTaskToGoalUseCase(
    private val goalInteractor: GoalInteractor,
    private val tasksNavigation: MainFlowNavigation,
    private val errorMessage: String
) {

    fun checkGoalAndOpenDialog(
        disposable: CompositeDisposable,
        goalId: Long,
        errorAction: (String) -> Unit
    ) {
        disposable.add(
            goalInteractor.checkGoalDone(goalId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        if (!it) {
                            tasksNavigation.navigateToAddNewElementFragment(goalId)
                        } else {
                            errorAction.invoke(errorMessage)
                        }
                    },
                    { it.printStackTrace() }
                )
        )
    }
}
package by.aermakova.todoapp.ui.goal.main

import android.util.Log
import androidx.lifecycle.LiveData
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.model.toCommonModelGoalList
import by.aermakova.todoapp.data.remote.auth.FirebaseAuthUtil
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.dialog.addItem.AddItemDialogNavigation
import by.aermakova.todoapp.ui.goal.GoalsNavigation
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Status
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Named

class GoalsViewModel @Inject constructor(
    private val navigation: MainFlowNavigation,
    @Named("ConfirmDialog") private val dialogNavigation: DialogNavigation<Boolean>,
    @Named("AddItemDialog") private val addItemDialogNavigation: AddItemDialogNavigation,
    private val goalInteractor: GoalInteractor
) : BaseViewModel() {

    val addNewElement = { navigation.navigateToAddNewElementFragment() }

    val logoutButton: (String) -> Unit = { confirmExit(it) }

    private fun confirmExit(message: String) {
        dialogNavigation.openItemDialog(message)
    }

    fun exit() {
        disposable.add(
            goalInteractor.removeAllFromLocalDatabase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    FirebaseAuthUtil.exit()
                    (navigation as GoalsNavigation).exit()
                }
                .subscribe {}
        )
    }

    private val _goalsList = PublishSubject.create<List<CommonModel>>()
    val goalsList: Observable<List<CommonModel>>
        get() = _goalsList.hide()

    val logoutObserver: LiveData<Boolean>?
        get() = dialogNavigation.getDialogResult()

    init {
        _status.onNext(Status.LOADING)
        disposable.add(
            goalInteractor.getAllGoalsWithKeyResults()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _goalsList.onNext(it.toCommonModelGoalList({ id ->
                            navigation.navigateToShowDetailsFragment(id)
                        }, {
                            Log.d("A_GoalsViewModel", "$it")
                        }))
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
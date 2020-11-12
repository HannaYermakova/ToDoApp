package by.aermakova.todoapp.ui.goal.main

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.model.toCommonModelGoalList
import by.aermakova.todoapp.data.model.toTextModel
import by.aermakova.todoapp.data.remote.auth.FirebaseAuthUtil
import by.aermakova.todoapp.data.useCase.GoalBottomSheetMenuUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.goal.GoalsNavigation
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.GoalsActionItem
import by.aermakova.todoapp.util.Status
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Named

const val INIT_SELECTED_GOAL_ID = -1L

class GoalsViewModel @Inject constructor(
    private val goalActionItems: Array<GoalsActionItem>,
    @Named("GoalsNavigation") private val navigation: MainFlowNavigation,
    @Named("ConfirmDialog") private val dialogNavigation: DialogNavigation<Boolean>,
    private val goalBottomSheetMenuUseCase: GoalBottomSheetMenuUseCase,
    private val goalInteractor: GoalInteractor,
    private val resources: Resources
) : BaseViewModel() {

    val addNewElement = { navigation.navigateToAddNewElementFragment() }

    val logoutButton: (String) -> Unit = { confirmExit(it) }

    private val openBottomSheetGoalsActions: (Long) -> Unit = {
        goalBottomSheetMenuUseCase.openBottomSheetGoalsActions(it, this)
    }

    val actionItems: LiveData<List<CommonModel>> = getLiveListOfGoalActionsItems()

    private fun getLiveListOfGoalActionsItems(): LiveData<List<CommonModel>> {
        val liveList = MutableLiveData<List<CommonModel>>()
        val list = goalActionItems
            .map { action ->
                action.toTextModel(resources) {
                    goalBottomSheetMenuUseCase.goalAction(action, disposable, errorAction)
                }
            }
        liveList.postValue(list)
        return liveList
    }

    val keyResultObserver: LiveData<String>?
        get() = goalBottomSheetMenuUseCase.addKeyResultToGoalUseCase.keyResultObserver


    fun addKeyResultToSelectedGoal(keyResultTitle: String) {
        goalBottomSheetMenuUseCase.addKeyResultToSelectedGoal(
            keyResultTitle,
            disposable,
            errorAction
        )
    }

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
                        _goalsList.onNext(
                            it.toCommonModelGoalList(
                                { id ->
                                    navigation.navigateToShowDetailsFragment(id)
                                }, openBottomSheetGoalsActions
                            )
                        )
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
package by.aermakova.todoapp.ui.goal.main

import androidx.lifecycle.LiveData
import by.aermakova.todoapp.data.di.scope.DialogConfirm
import by.aermakova.todoapp.data.di.scope.NavigationGoals
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.model.FunctionLong
import by.aermakova.todoapp.data.model.FunctionString
import by.aermakova.todoapp.data.model.toCommonModelGoalList
import by.aermakova.todoapp.data.remote.auth.FirebaseAuthUtil
import by.aermakova.todoapp.data.useCase.bottomMenu.GoalBottomSheetMenuUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.goal.GoalsNavigation
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Status
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

const val INIT_SELECTED_ITEM_ID = -1L

class GoalsViewModel @Inject constructor(
    @NavigationGoals private val navigation: MainFlowNavigation,
    @DialogConfirm private val dialogNavigation: DialogNavigation<Boolean>,
    private val goalBottomSheetMenuUseCase: GoalBottomSheetMenuUseCase,
    private val goalInteractor: GoalInteractor
) : BaseViewModel() {

    override val mainFlowNavigation: MainFlowNavigation
        get() = navigation

    val addNewElement = { navigation.navigateToAddNewElementFragment() }

    val logoutButton: FunctionString = { confirmExit(it) }

    private val openBottomSheetGoalsActions: FunctionLong = {
        goalBottomSheetMenuUseCase.openBottomSheetActions(disposable, it, this, errorAction)
    }

    val actionItems: LiveData<List<CommonModel>> = goalBottomSheetMenuUseCase.liveListOfItemsActionsItems

    val keyResultObserver: LiveData<String>?
        get() = goalBottomSheetMenuUseCase.addKeyResultToGoalUseCase.keyResultObserver

    val confirmDeleteListener = goalBottomSheetMenuUseCase.deleteItemUseCase.deleteConfirmObserver

    fun addKeyResultToSelectedGoal(keyResultTitle: String) {
        goalBottomSheetMenuUseCase.addKeyResultToSelectedGoal(
            keyResultTitle,
            disposable,
            errorAction
        )
    }

    fun confirmDelete(value: Boolean?) {
        goalBottomSheetMenuUseCase.deleteItemUseCase.confirmDelete(value)
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
        loadingAction.invoke()
        disposable.add(
            goalInteractor.getAllGoalsWithKeyResults()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _goalsList.onNext(
                            it.toCommonModelGoalList(
                                { navigation.navigateToShowDetailsFragment(it) },
                                openBottomSheetGoalsActions
                            )
                        )
                        successAction.invoke()
                    },
                    {
                        it.printStackTrace()
                        _status.onNext(Status.ERROR)
                    }
                )
        )
    }
}
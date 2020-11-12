package by.aermakova.todoapp.ui.goal.main

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.model.toCommonModelGoalList
import by.aermakova.todoapp.data.model.toTextModel
import by.aermakova.todoapp.data.remote.auth.FirebaseAuthUtil
import by.aermakova.todoapp.data.useCase.AddKeyResultToGoalUseCase
import by.aermakova.todoapp.databinding.BottomSheetGoalActionBinding
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.goal.GoalsNavigation
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.GoalsActionItem
import by.aermakova.todoapp.util.Status
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Named

const val INIT_SELECTED_GOAL_ID = -1L

class GoalsViewModel @Inject constructor(
    private val goalActionItems: Array<GoalsActionItem>,
    private val navigation: MainFlowNavigation,
    @Named("ConfirmDialog") private val dialogNavigation: DialogNavigation<Boolean>,
    val addKeyResultToGoalUseCase: AddKeyResultToGoalUseCase,
    private val goalActionBind: BottomSheetGoalActionBinding,
    private val dialog: BottomSheetDialog,
    private val goalInteractor: GoalInteractor,
    private val resources: Resources
) : BaseViewModel() {

    val addNewElement = { navigation.navigateToAddNewElementFragment() }

    val logoutButton: (String) -> Unit = { confirmExit(it) }

    private var selectedGoalId = INIT_SELECTED_GOAL_ID

    private val openBottomSheetGoalsActions: (Long) -> Unit = {
        goalActionBind.viewModel = this
        selectedGoalId = it
        dialog.setContentView(goalActionBind.root)
        dialog.show()
    }

    val actionItems: LiveData<List<CommonModel>> = getLiveListOfGoalActionsItems()

    private fun getLiveListOfGoalActionsItems(): LiveData<List<CommonModel>> {
        val liveList = MutableLiveData<List<CommonModel>>()
        val list = goalActionItems
            .map { action ->
                action.toTextModel(resources) {
                    dialog.dismiss()
                    goalAction(action)
                }
            }
        liveList.postValue(list)
        return liveList
    }

    private fun goalAction(action: GoalsActionItem) {
        when (action) {
            GoalsActionItem.ADD_KEY_RESULT_TO_GOAL -> addKeyResultToGoalUseCase.openDialog(
                selectedGoalId
            )
            GoalsActionItem.ADD_STEP_TO_GOAL -> Log.d("A_GoalsViewModel", "ADD_STEP_TO_GOAL")
            GoalsActionItem.ADD_TASK_TO_GOAL -> Log.d("A_GoalsViewModel", "ADD_TASK_TO_GOAL")
            GoalsActionItem.ADD_IDEA_TO_GOAL -> Log.d("A_GoalsViewModel", "ADD_IDEA_TO_GOAL")
        }
        selectedGoalId = INIT_SELECTED_GOAL_ID
    }

    fun addKeyResultToSelectedGoal(keyResultTitle: String) {
        addKeyResultToGoalUseCase.addKeyResult(
            keyResultTitle,
            disposable
        ) { _status.onNext(Status.ERROR.apply { message = it }) }
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
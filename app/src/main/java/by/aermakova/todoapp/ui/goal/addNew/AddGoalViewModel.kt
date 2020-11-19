package by.aermakova.todoapp.ui.goal.addNew

import androidx.lifecycle.LiveData
import by.aermakova.todoapp.data.di.scope.NavigationGoals
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.model.toCommonModelStringList
import by.aermakova.todoapp.data.useCase.CreateGoalUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class AddGoalViewModel @Inject constructor(
    @NavigationGoals private val navigation: MainFlowNavigation,
    private val dialogNavigation: DialogNavigation<String>,
    private val createGoalUseCase: CreateGoalUseCase
) : BaseViewModel() {

    override val mainFlowNavigation: MainFlowNavigation
        get() = navigation

    val addKeyResult: (String) -> Unit = { dialogNavigation.openItemDialog(it) }

    val saveGoal = { saveGoalToLocalDataBaseAndSyncToRemote() }

    val keyResultObserver: LiveData<String>?
        get() = dialogNavigation.getDialogResult()

    private val _tempKeyResultsList = BehaviorSubject.create<List<CommonModel>>()
    val tempKeyResultsList: Observable<List<CommonModel>>
        get() = _tempKeyResultsList.hide()

    private val _tempGoalTitle = BehaviorSubject.create<String>()
    val tempGoalTitle: Observer<String>
        get() = _tempGoalTitle

    private val _tempKeyResult = BehaviorSubject.create<List<String>>()

    private val tempKeyResults = arrayListOf<String>()

    fun addTempKeyResult(tempKeyResult: String) {
        tempKeyResults.add(tempKeyResult)
        _tempKeyResult.onNext(tempKeyResults)
    }

    init {
        disposable.add(
            _tempKeyResult
                .subscribe(
                    { _tempKeyResultsList.onNext(it.toCommonModelStringList()) },
                    { it.printStackTrace() })
        )
    }

    private fun saveGoalToLocalDataBaseAndSyncToRemote() {
        loadingAction.invoke()
        createGoalUseCase.saveGoalToLocalDataBaseAndSyncToRemote(
            disposable,
            _tempGoalTitle.value,
            tempKeyResults,
            { mainFlowNavigation.popBack() },
            errorAction
        )
    }
}
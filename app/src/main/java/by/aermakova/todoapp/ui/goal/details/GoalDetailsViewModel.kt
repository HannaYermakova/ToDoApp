package by.aermakova.todoapp.ui.goal.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.ui.adapter.GoalModel
import by.aermakova.todoapp.ui.adapter.toCommonModel
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GoalDetailsViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    private val dialogNavigation: DialogNavigation<String>,
    private val goalInteractor: GoalInteractor,
    private val goalId: Long
) : BaseViewModel() {

    init {
        disposable.add(
            goalInteractor.getGoalWithKeyResultsById(goalId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _goalModel.postValue(it.toCommonModel {  })
                }, {
                    it.printStackTrace()
                })
        )
    }

    val popBack = { mainFlowNavigation.popBack() }

    private val _goalModel = MutableLiveData<GoalModel>()
    val goalModel : LiveData<GoalModel>
        get() = _goalModel
}
package by.aermakova.todoapp.ui.goal

import android.util.Log
import androidx.lifecycle.LiveData
import by.aermakova.todoapp.BR
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.databinding.ItemTextLineBinding
import by.aermakova.todoapp.databinding.ItemTextLineBindingImpl
import by.aermakova.todoapp.ui.adapter.Model
import by.aermakova.todoapp.ui.adapter.toModel
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class AddGoalViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    private val dialogNavigation: DialogNavigation,
    private val goalInteractor: GoalInteractor
) : BaseViewModel() {

    val popBack = { mainFlowNavigation.popBack() }

    val addKeyResult: (String) -> Unit = {
        dialogNavigation.openAddItemDialog(it)
    }

    val saveGoal = { saveGoalToLocalDataBase() }

    private fun saveGoalToLocalDataBase() {
        disposable.add(
            Single.create<Boolean> {
                if (!_tempGoalTitle.value.isNullOrBlank()) {
                    goalInteractor.saveGoal(_tempGoalTitle.value!!, tempKeyResults)
                    it.onSuccess(true)
                } else {
                    Log.i("AddGoalViewModel", "_tempGoalTitle is blank")
                    it.onSuccess(false)
                }
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it) mainFlowNavigation.popBack()
                },
                    { it.printStackTrace() })
        )
    }

    val keyResultObserver: LiveData<String>?
        get() = dialogNavigation.getDialogResult()

    private val _tempKeyResultsList = BehaviorSubject.create<List<Model<String>>>()
    val tempKeyResultsList: Observable<List<Model<String>>>
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
        _disposable.add(
            _tempKeyResult
                .subscribe({
                    _tempKeyResultsList.onNext(it.toModelStringList())
                },
                    { it.printStackTrace() })
        )
    }
}

fun List<String>.toModelStringList(): List<Model<String>> {
    var i = 0L
    return map { toModel(i++, it, R.layout.item_text_line, BR.text) }
}
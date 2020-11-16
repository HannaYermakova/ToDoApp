package by.aermakova.todoapp.data.useCase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.util.handleError
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class ChangeGoalTextUseCase(
    private val goalInteractor: GoalInteractor,
    private val errorMessage: String
) {

    private val _existingGoalTitle = MutableLiveData<String>()
    val existingGoalTitle: LiveData<String>
        get() = _existingGoalTitle

    fun setExistingGoalTitle(title: String) {
        _existingGoalTitle.postValue(title)
    }

    private val _newGoalTitle = BehaviorSubject.create<String>()
    val newGoalTitle: Observer<String>
        get() = _newGoalTitle

    fun saveChanges(
        goalId: Long,
        disposable: CompositeDisposable,
        saveSuccess: Observer<Boolean>,
        errorAction: (String) -> Unit
    ) {
        val newText = _newGoalTitle.value
        if (newText != null
            && _existingGoalTitle.value != newText
            && !newText.isNullOrBlank()
        ) {
            disposable.add(
                Single.create<Boolean> {
                    it.onSuccess(goalInteractor.updateGoalTextLocal(newText, goalId))
                }
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            goalInteractor.getGoalById(goalId)
                                .subscribe(
                                    {
                                        goalInteractor.updateGoalTextRemote(it)
                                        saveSuccess.onNext(true)
                                    },
                                    {
                                        it.handleError(errorMessage, errorAction)
                                        saveSuccess.onNext(false)
                                    }
                                )
                        },
                        {
                            it.handleError(errorMessage, errorAction)
                            saveSuccess.onNext(false)
                        }
                    )
            )
        } else {
            saveSuccess.onNext(true)
        }
    }
}
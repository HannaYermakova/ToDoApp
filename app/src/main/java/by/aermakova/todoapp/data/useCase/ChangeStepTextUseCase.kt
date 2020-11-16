package by.aermakova.todoapp.data.useCase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.util.handleError
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class ChangeStepTextUseCase(
    private val stepInteractor: StepInteractor,
    private val errorMessage: String
) {

    private val _existingStepTitle = MutableLiveData<String>()
    val existingStepTitle: LiveData<String>
        get() = _existingStepTitle

    fun setExistingStepTitle(title: String) {
        _existingStepTitle.postValue(title)
    }

    private val _newStepTitle = BehaviorSubject.create<String>()
    val newStepTitle: Observer<String>
        get() = _newStepTitle

    fun saveChanges(
        stepId: Long,
        disposable: CompositeDisposable,
        saveSuccess: Observer<Boolean>,
        errorAction: (String) -> Unit
    ) {
        val newText = _newStepTitle.value
        if (newText != null
            && _existingStepTitle.value != newText
            && !newText.isNullOrBlank()
        ) {
            disposable.add(
                Single.create<Boolean> {
                    it.onSuccess(stepInteractor.updateStepTextLocal(newText, stepId))
                }
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            stepInteractor.getStepById(stepId)
                                .subscribe(
                                    {
                                        stepInteractor.updateStepToRemote(it)
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
package by.aermakova.todoapp.data.useCase.editText

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.model.FunctionString
import by.aermakova.todoapp.util.handleError
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class ChangeItemTextUseCase<Entity>(
    private val interactor: ChangeItemText<Entity>,
    private val errorMessage: String
) {

    private val _existingItemText = MutableLiveData<String>()
    val existingItemText: LiveData<String>
        get() = _existingItemText

    fun setExistingItemText(title: String) {
        _existingItemText.postValue(title)
    }

    private val _newItemText = BehaviorSubject.create<String>()
    val newItemText: Observer<String>
        get() = _newItemText

    fun saveChanges(
        itemId: Long,
        disposable: CompositeDisposable,
        saveSuccess: Observer<Boolean>,
        errorAction: FunctionString
    ) {
        val newText = _newItemText.value
        if (newText != null
            && _existingItemText.value != newText
            && !newText.isNullOrBlank()
        ) {
            disposable.add(
                Single.create<Boolean> {
                    it.onSuccess(interactor.updateItemTextLocal(newText, itemId))
                }
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            interactor.getItemById(itemId)
                                .subscribeOn(Schedulers.io())
                                .subscribe(
                                    {
                                        interactor.updateItemToRemote(it)
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
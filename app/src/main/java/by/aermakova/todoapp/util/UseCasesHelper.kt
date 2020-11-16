package by.aermakova.todoapp.util

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.aermakova.todoapp.data.model.CommonModel
import io.reactivex.disposables.CompositeDisposable


fun <Type : ActionTextConverter> Array<Type>.getLiveListOfActionsItems(
    disposable: CompositeDisposable,
    errorAction: (String) -> Unit,
    resources: Resources,
    itemAction: (
        Type,
        CompositeDisposable,
        (String) -> Unit
    ) -> Unit
): LiveData<List<CommonModel>> {
    val liveList = MutableLiveData<List<CommonModel>>()
    val list = this
        .map { action ->
            action.toTextModel(resources) {
                itemAction.invoke(action, disposable, errorAction)
            }
        }
    liveList.postValue(list)
    return liveList
}
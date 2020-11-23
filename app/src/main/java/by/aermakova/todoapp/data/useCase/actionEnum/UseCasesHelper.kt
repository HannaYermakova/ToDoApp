package by.aermakova.todoapp.data.useCase.actionEnum

import android.content.res.Resources
import by.aermakova.todoapp.data.model.CommonModel
import by.aermakova.todoapp.data.model.FunctionString
import io.reactivex.disposables.CompositeDisposable


fun <Type : ActionTextConverter> Array<Type>.getLiveListOfActionsItems(
    disposable: CompositeDisposable,
    errorAction: FunctionString,
    resources: Resources,
    itemIsDone: Boolean,
    itemAction: (
        Type,
        CompositeDisposable,
        FunctionString
    ) -> Unit
): List<CommonModel> {
    return filter { if (itemIsDone) it.forDone else true }
        .map { action ->
            action.toTextModel(resources) {
                itemAction.invoke(action, disposable, errorAction)
            }
        }
}
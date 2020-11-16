package by.aermakova.todoapp.data.useCase

import by.aermakova.todoapp.data.interactor.CheckItemIsDone
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Item
import by.aermakova.todoapp.util.handleError
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AddItemToParentItemUseCase<Navigator : MainFlowNavigation>(
    private val itemIsDone: CheckItemIsDone,
    private val item: Item,
    private val navigation: Navigator,
    private val errorMessage: String
) {

    fun checkGoalAndOpenDialog(
        disposable: CompositeDisposable,
        itemId: Long,
        errorAction: (String) -> Unit
    ) {
        disposable.add(
            itemIsDone.checkIsDone(itemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        if (!it) {
                            navigation.navigateToAddNewElementFragment(itemId, item)
                        } else {
                            errorAction.invoke(errorMessage)
                        }
                    },
                    { it.handleError(errorMessage, errorAction) }
                )
        )
    }
}
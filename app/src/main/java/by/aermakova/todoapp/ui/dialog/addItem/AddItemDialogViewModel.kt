package by.aermakova.todoapp.ui.dialog.addItem

import by.aermakova.todoapp.data.di.scope.ErrorEmptyField
import by.aermakova.todoapp.data.di.scope.TitleDialogAddItem
import by.aermakova.todoapp.ui.base.BaseDialogVieModel
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.util.Status
import io.reactivex.Observer
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class AddItemDialogViewModel @Inject constructor(
    @TitleDialogAddItem val title: String,
    @ErrorEmptyField val errorMessage: String,
    private val router: DialogNavigation<String>
) : BaseDialogVieModel() {

    private val _tempItemTitle = BehaviorSubject.create<String>()
    val tempItemTitle: Observer<String>
        get() = _tempItemTitle

    val saveItem = { doOnOk() }

    override fun doOnOk() {
        val title = _tempItemTitle.value
        if (!title.isNullOrBlank()) {
            router.setDialogResult(title.trim())
            _dismissCommand.onNext(true)
        } else {
            _status.onNext(Status.ERROR.apply { message = errorMessage })
        }
    }

    override fun doOnCancel() {

    }
}
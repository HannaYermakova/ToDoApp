package by.aermakova.todoapp.ui.dialog.selectItem

import by.aermakova.todoapp.ui.adapter.CommonModel
import by.aermakova.todoapp.ui.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class SelectItemViewModel : BaseViewModel() {

    protected val _itemList = PublishSubject.create<List<CommonModel>>()
    val itemList: Observable<List<CommonModel>>
        get() = _itemList.hide()

    protected val _dismissCommand = PublishSubject.create<Boolean>()
    val dismissCommand: Observable<Boolean>
        get() = _dismissCommand.hide()
}
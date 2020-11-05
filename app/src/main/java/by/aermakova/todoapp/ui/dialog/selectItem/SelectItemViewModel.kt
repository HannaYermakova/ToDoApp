package by.aermakova.todoapp.ui.dialog.selectItem

import by.aermakova.todoapp.ui.adapter.CommonModel
import by.aermakova.todoapp.ui.base.BaseDialogVieModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class SelectItemViewModel : BaseDialogVieModel() {

    protected val _itemList = PublishSubject.create<List<CommonModel>>()
    val itemList: Observable<List<CommonModel>>
        get() = _itemList.hide()
}
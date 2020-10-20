package by.aermakova.todoapp.ui.dialog.selectItem

import by.aermakova.todoapp.ui.adapter.ModelWrapper
import by.aermakova.todoapp.ui.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class SelectItemViewModel<Type> : BaseViewModel() {

    private val _itemList = PublishSubject.create<List<ModelWrapper<Type>>>()
    val itemList: Observable<List<ModelWrapper<Type>>>
        get() = _itemList.hide()
}
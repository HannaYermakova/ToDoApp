package by.aermakova.todoapp.ui.dialog.selectItem

import by.aermakova.todoapp.ui.adapter.ModelWrapper
import by.aermakova.todoapp.ui.base.BaseViewModel
import io.reactivex.Observable

abstract class SelectItemViewModel<Type> : BaseViewModel() {

    val itemList: Observable<List<ModelWrapper<Type>>>
        get() = getItemsList()

    abstract fun getItemsList():Observable<List<ModelWrapper<Type>>>
}
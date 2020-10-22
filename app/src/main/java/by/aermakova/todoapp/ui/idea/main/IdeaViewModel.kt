package by.aermakova.todoapp.ui.idea.main

import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.ui.adapter.CommonModel
import by.aermakova.todoapp.ui.adapter.toCommonModel
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class IdeaViewModel @Inject constructor(
    private val mainFlowNavigation: MainFlowNavigation,
    private val ideaInteractor: IdeaInteractor
) : BaseViewModel() {

    private val _ideasList = PublishSubject.create<List<CommonModel>>()
    val ideasList: Observable<List<CommonModel>>
        get() = _ideasList.hide()

    val addNewElement = { mainFlowNavigation.navigateToAddNewElementFragment() }

    init {
        disposable.add(
            ideaInteractor.getAllIdeas()
                .map { list -> list.map { it.toCommonModel { } } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _ideasList.onNext(it) },
                    { it.printStackTrace() }
                )
        )
    }
}
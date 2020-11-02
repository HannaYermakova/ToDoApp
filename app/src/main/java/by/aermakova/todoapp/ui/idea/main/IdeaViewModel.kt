package by.aermakova.todoapp.ui.idea.main

import android.util.Log
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.ui.adapter.CommonModel
import by.aermakova.todoapp.ui.adapter.toCommonModel
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import by.aermakova.todoapp.util.Status
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
        _status.onNext(Status.LOADING)
        disposable.add(
            ideaInteractor.getAllIdeas()
                .map { list ->
                    list.map {
                        it.toCommonModel { id ->
                            mainFlowNavigation.navigateToShowDetailsFragment(
                                id
                            )
                        }
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.d("IdeaViewModel", "Status.SUCCESS")
                        _status.onNext(Status.SUCCESS)
                        _ideasList.onNext(it)
                    },
                    {
                        Log.d("IdeaViewModel", "Status.ERROR")
                        _status.onNext(Status.ERROR)
                        it.printStackTrace()
                    }
                )
        )
    }
}
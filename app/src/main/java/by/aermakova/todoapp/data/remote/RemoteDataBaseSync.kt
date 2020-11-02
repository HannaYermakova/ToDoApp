package by.aermakova.todoapp.data.remote

import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.remote.model.GoalRemoteModel
import by.aermakova.todoapp.data.remote.model.KeyResultRemoteModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class RemoteDataBaseSync(
    private val goalInteractor: GoalInteractor
) {

    private lateinit var finishSync: () -> Unit

    fun startSync(disposable: CompositeDisposable, finishAction: () -> Unit) {
        syncGoalsRemoteDataBase(disposable)
        finishSync = finishAction
    }

    private fun syncGoalsRemoteDataBase(disposable: CompositeDisposable) {
        val dataObserver = PublishSubject.create<List<GoalRemoteModel>>()
        goalInteractor.addGoalsDataListener(dataObserver)
        disposable.add(
            dataObserver
                .observeOn(Schedulers.io())
                .subscribe(
                    {
                        goalInteractor.saveGoalsInLocalDatabase(it)
                        syncKeyResultsRemoteDataBase(disposable)
                    },
                    { it.printStackTrace() }
                )
        )
    }

    private fun syncKeyResultsRemoteDataBase(disposable: CompositeDisposable) {
        val dataObserver = PublishSubject.create<List<KeyResultRemoteModel>>()
        goalInteractor.addKeyResultsDataListener(dataObserver)
        disposable.add(
            dataObserver
                .observeOn(Schedulers.io())
                .subscribe(
                    {
                        goalInteractor.saveKeyResultsInLocalDatabase(it)
                        finishSync.invoke()
                    },
                    { it.printStackTrace() }
                )
        )
    }
}
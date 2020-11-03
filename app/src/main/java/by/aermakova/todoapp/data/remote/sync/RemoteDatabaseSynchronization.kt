package by.aermakova.todoapp.data.remote.sync

import by.aermakova.todoapp.data.interactor.*
import by.aermakova.todoapp.data.remote.model.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class RemoteDatabaseSynchronization(
    private val goalInteractor: GoalInteractor,
    private val keyResultInteractor: KeyResultInteractor,
    private val stepInteractor: StepInteractor,
    private val taskInteractor: TaskInteractor,
    private val ideaInteractor: IdeaInteractor
) {

    private lateinit var disposable: CompositeDisposable

    fun startSync(compositeDisposable: CompositeDisposable, finishAction: () -> Unit) {
        disposable = compositeDisposable
        syncItemsRemoteDataBase(goalInteractor) {
            syncItemsRemoteDataBase(keyResultInteractor) {
                syncItemsRemoteDataBase(stepInteractor) {
                    syncItemsRemoteDataBase(taskInteractor) {
                        syncItemsRemoteDataBase(ideaInteractor) {
                            finishAction.invoke()
                        }
                    }
                }
            }
        }
    }

    private fun <RemoteModel : BaseRemoteModel> syncItemsRemoteDataBase(
        interactor: RemoteSync<RemoteModel>,
        nextAction: () -> Unit
    ) {
        val dataObserver = PublishSubject.create<List<RemoteModel>>()
        interactor.addItemsDataListener(dataObserver)
        disposable.add(
            dataObserver
                .observeOn(Schedulers.io())
                .doOnNext { nextAction.invoke() }
                .subscribe(
                    {
                        interactor.saveItemsInLocalDatabase(it)
                    },
                    { it.printStackTrace() }
                )
        )
    }
}
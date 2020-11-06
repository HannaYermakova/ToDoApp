package by.aermakova.todoapp.data.interactor

import by.aermakova.todoapp.data.db.entity.StepEntity
import by.aermakova.todoapp.data.remote.RemoteDatabase
import by.aermakova.todoapp.data.remote.model.StepRemoteModel
import by.aermakova.todoapp.data.remote.model.toLocal
import by.aermakova.todoapp.data.remote.model.toRemote
import by.aermakova.todoapp.data.remote.sync.RemoteSync
import by.aermakova.todoapp.data.repository.StepRepository
import by.aermakova.todoapp.ui.adapter.TextModel
import by.aermakova.todoapp.ui.adapter.toTextModel
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class StepInteractor(
    private val stepRepository: StepRepository,
    private val stepRemoteDatabase: RemoteDatabase<StepRemoteModel>
) : RemoteSync<StepRemoteModel> {

    fun getStepsByKeyResultId(keyResultId: Long): Observable<List<StepEntity>> {
        return stepRepository.getStepsByKeyResultId(keyResultId)
    }

    fun getStepById(stepId: Long): Single<StepEntity> {
        return stepRepository.getStepById(stepId)
    }

    private fun saveStepInLocalDatabase(stepEntity: StepEntity): Long {
        return stepRepository.saveStepEntity(stepEntity)
    }

    private fun saveTaskToRemote(entity: StepEntity) {
        stepRemoteDatabase.saveData(entity.toRemote())
    }

    fun getAllSteps(): Observable<List<StepEntity>> {
        return stepRepository.getAllSteps()
    }

    fun createStep(text: String, goalId: Long, keyResultId: Long): Single<Disposable> {
        return Single.create<Long> {
            it.onSuccess(saveStepInLocalDatabase(createStepEntity(text, goalId, keyResultId)))
        }
            .map {
                getStepById(it).subscribe { entity ->
                    saveTaskToRemote(entity)
                }
            }
    }

    private fun createStepEntity(text: String, goalId: Long, keyResultId: Long) =
        StepEntity(
            stepGoalId = goalId,
            stepKeyResultId = keyResultId,
            text = text
        )

    fun updateStep(status: Boolean, stepId: Long): Boolean {
        stepRepository.updateStatus(status, stepId)
        return true
    }

    fun updateStepToRemote(step: StepEntity?) {
        step?.let { stepRemoteDatabase.updateData(it.toRemote()) }
    }

    override fun addItemsDataListener(dataObserver: Observer<List<StepRemoteModel>>) {
        stepRemoteDatabase.addDataListener(dataObserver)
    }

    override fun saveItemsInLocalDatabase(list: List<StepRemoteModel>) {
        stepRepository.saveSteps(list.map { it.toLocal() })
    }

    fun getUndoneStepsByKeyResultId(keyResultId: Long): Single<List<StepEntity>> {
        return stepRepository.getUndoneStepsByKeyResultId(keyResultId)
    }

    fun createStepsList(keyResultId: Long, stepsList : Observer<List<TextModel>>): Disposable {
       return getStepsByKeyResultId(keyResultId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { entity -> entity.map { it.toTextModel() } }
            .subscribe(
                { stepsList.onNext(it) },
                { it.printStackTrace() }
            )
    }
}
package by.aermakova.todoapp.data.interactor

import by.aermakova.todoapp.data.db.entity.StepEntity
import by.aermakova.todoapp.data.remote.RemoteDatabase
import by.aermakova.todoapp.data.remote.model.StepRemoteModel
import by.aermakova.todoapp.data.remote.model.toRemote
import by.aermakova.todoapp.data.repository.StepRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable

class StepInteractor(
    private val stepRepository: StepRepository,
    private val stepRemoteDatabase: RemoteDatabase<StepRemoteModel>
) {

    fun getStepsByKeyResultId(keyResultId: Long): Observable<List<StepEntity>> {
        return stepRepository.getStepsByKeyResultId(keyResultId)
    }

    fun getStepById(stepId: Long): Observable<StepEntity> {
        return stepRepository.getStepById(stepId)
    }

    fun saveStepInLocalDatabase(stepEntity: StepEntity): Long {
        return stepRepository.saveStepEntity(stepEntity)
    }

    fun saveTaskToRemote(entity: StepEntity) {
        stepRemoteDatabase.saveData(entity.toRemote())
    }

    fun getAllSteps(): Observable<List<StepEntity>> {
        return stepRepository.getAllSteps()
    }

    fun createStep(text: String, goalId: Long, keyResultId: Long): Single<Disposable> {
        return Single.create<Long> {
            it.onSuccess(
                saveStepInLocalDatabase(createStepEntity(text, goalId, keyResultId))
            )
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
}
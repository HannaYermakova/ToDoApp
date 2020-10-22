package by.aermakova.todoapp.data.interactor

import by.aermakova.todoapp.data.db.entity.StepEntity
import by.aermakova.todoapp.data.remote.RemoteDatabase
import by.aermakova.todoapp.data.remote.model.StepRemoteModel
import by.aermakova.todoapp.data.remote.model.toRemote
import by.aermakova.todoapp.data.repository.StepRepository
import by.aermakova.todoapp.ui.adapter.StepModel
import by.aermakova.todoapp.ui.adapter.toCommonModel
import io.reactivex.Observable

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

    fun saveStepInLocalDatabase(text: String, goalId: Long, keyResultId: Long): Long {
        return stepRepository.saveStepEntity(
            StepEntity(
                stepGoalId = goalId,
                stepKeyResultId = keyResultId,
                text = text
            )
        )
    }

    fun saveTaskToRemote(entity: StepEntity) {
        stepRemoteDatabase.saveData(entity.toRemote())
    }

    fun getAllSteps(): Observable<List<StepEntity>> {
        return stepRepository.getAllSteps()
    }
}
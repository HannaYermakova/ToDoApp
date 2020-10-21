package by.aermakova.todoapp.data.interactor

import by.aermakova.todoapp.data.db.entity.StepEntity
import by.aermakova.todoapp.data.repository.StepRepository
import io.reactivex.Observable

class StepInteractor (
    private val stepRepository: StepRepository
) {

    fun getStepsByKeyResultId(keyResultId: Long): Observable<List<StepEntity>> {
        return stepRepository.getStepsByKeyResultId(keyResultId)
    }

    fun getStepById(stepId: Long): Observable<StepEntity> {
        return stepRepository.getStepById(stepId)
    }
}
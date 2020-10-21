package by.aermakova.todoapp.data.repository

import by.aermakova.todoapp.data.db.dao.StepDao
import by.aermakova.todoapp.data.db.entity.StepEntity
import io.reactivex.Observable
import javax.inject.Inject

class StepRepository @Inject constructor(
    private val stepDao: StepDao
) {

    fun getStepsByKeyResultId(keyResultId: Long): Observable<List<StepEntity>> {
        return stepDao.getStepByKeyResultId(keyResultId)
    }

    fun getStepById(stepId: Long): Observable<StepEntity> {
        return stepDao.getStepById(stepId)
    }
}
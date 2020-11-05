package by.aermakova.todoapp.data.repository

import by.aermakova.todoapp.data.db.dao.StepDao
import by.aermakova.todoapp.data.db.entity.KeyResultEntity
import by.aermakova.todoapp.data.db.entity.StepEntity
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class StepRepository @Inject constructor(
    private val stepDao: StepDao
) {

    fun getStepsByKeyResultId(keyResultId: Long): Observable<List<StepEntity>> {
        return stepDao.getStepByKeyResultId(keyResultId)
    }

    fun getStepById(stepId: Long): Single<StepEntity> {
        return stepDao.getStepById(stepId)
    }

    fun saveStepEntity(stepEntity: StepEntity): Long {
        return stepDao.insertStep(stepEntity)
    }

    fun getAllSteps(): Observable<List<StepEntity>> {
        return stepDao.getAllSteps()
    }

    fun updateStatus(status: Boolean, stepId: Long) {
        stepDao.updateStatus(status, stepId)
    }

    fun getStepsByGoalId(goalId: Long): Single<List<StepEntity>> {
        return stepDao.getStepByGoalId(goalId)
    }

    fun getStepsByKeyResultIds(keyResIds: List<Long>): Single<List<StepEntity>> {
        return stepDao.getStepByKeyResultIds(keyResIds)
    }

    fun saveSteps(stepsEntities: List<StepEntity>) {
        stepDao.insertAllSteps(stepsEntities)
    }

    fun getUndoneStepsByKeyResultId(keyResultId: Long): Single<List<StepEntity>> {
        return stepDao.getUndoneStepsByKeyResultId(keyResultId)
    }
}
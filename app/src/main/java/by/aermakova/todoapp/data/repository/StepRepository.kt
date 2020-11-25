package by.aermakova.todoapp.data.repository

import by.aermakova.todoapp.data.db.dao.StepDao
import by.aermakova.todoapp.data.db.entity.StepEntity
import io.reactivex.Single
import javax.inject.Inject

class StepRepository @Inject constructor(
    private val stepDao: StepDao
) {

    fun getStepsByKeyResultId(keyResultId: Long) = stepDao.getStepByKeyResultId(keyResultId)

    fun getStepById(stepId: Long) = stepDao.getStepById(stepId)

    fun saveStepEntity(stepEntity: StepEntity) = stepDao.insertStep(stepEntity)

    fun getAllSteps() = stepDao.getAllSteps()

    fun updateStepStatus(status: Boolean, stepId: Long) {
        stepDao.updateStatus(status, stepId)
    }

    fun getStepsByGoalId(goalId: Long) = stepDao.getStepByGoalId(goalId)

    fun getStepsByKeyResultIds(keyResIds: List<Long>) = stepDao.getStepByKeyResultIds(keyResIds)

    fun saveSteps(stepsEntities: List<StepEntity>) {
        stepDao.insertAllStepsTransaction(stepsEntities)
    }

    fun getAllStepsIdByGoalId(goalId: Long): Single<List<Long>> =
        stepDao.getAllStepsIdByGoalId(goalId)

    fun checkStepIsDone(id: Long) = stepDao.checkStepIsDone(id)

    fun updateStepText(newText: String, stepId: Long) = stepDao.updateStepText(newText, stepId) > 0
}
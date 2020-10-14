package by.aermakova.todoapp.data.repository

import by.aermakova.todoapp.data.db.dao.GoalDao
import by.aermakova.todoapp.data.db.dao.KeyResultDao
import by.aermakova.todoapp.data.db.entity.GoalEntity
import by.aermakova.todoapp.data.db.entity.KeyResultEntity
import javax.inject.Inject

class GoalRepository @Inject constructor(
    private val goalDao: GoalDao,
    private val keyResultDao: KeyResultDao
) {

    fun saveGoalInLocalDataBase(goalEntity: GoalEntity): Long {
        return goalDao.insertGoal(goalEntity)
    }

    fun saveKeyResults(keyResultEntities: List<KeyResultEntity>) {
        keyResultDao.insertAllKeyResults(keyResultEntities)
    }
}
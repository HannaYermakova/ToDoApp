package by.aermakova.todoapp.data.repository

import by.aermakova.todoapp.data.db.dao.GoalDao
import by.aermakova.todoapp.data.db.dao.KeyResultDao
import by.aermakova.todoapp.data.db.entity.GoalEntity
import by.aermakova.todoapp.data.db.entity.GoalKeyResults
import by.aermakova.todoapp.data.db.entity.KeyResultEntity
import io.reactivex.Observable
import javax.inject.Inject

class GoalRepository @Inject constructor(
    private val goalDao: GoalDao,
    private val keyResultDao: KeyResultDao
) {

    fun saveGoalInLocalDataBase(goalEntity: GoalEntity): Long {
        return goalDao.insertGoal(goalEntity)
    }

    fun saveGoalsInLocalDataBase(goalEntities: List<GoalEntity>) {
        goalDao.insertAllGoals(goalEntities)
    }

    fun saveKeyResults(keyResultEntities: List<KeyResultEntity>) {
        keyResultDao.insertAllKeyResults(keyResultEntities)
    }

    fun getAllGoalsWithKeyResults(): Observable<List<GoalKeyResults>> {
        return goalDao.getAllGoalsWithKeyResults()
    }

    fun getAllGoals(): Observable<List<GoalEntity>> {
        return goalDao.getAllGoals()
    }

    fun getKeyResultById(keyResultId: Long): Observable<KeyResultEntity>{
        return keyResultDao.getKeyResultById(keyResultId)
    }

    fun getGoalWithKeyResultsById(id: Long): Observable<GoalKeyResults> {
        return goalDao.getGoalWithKeyResultsById(id)
    }
}
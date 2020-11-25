package by.aermakova.todoapp.data.db.dao

import androidx.room.*
import by.aermakova.todoapp.data.db.entity.IdeaEntity
import by.aermakova.todoapp.data.db.entity.KeyResultEntity
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface KeyResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKeyResult(keyResult: KeyResultEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllKeyResults(keyResults: List<KeyResultEntity>)

    @Transaction
    fun insertAllKeyResultsTransaction(keyResults: List<KeyResultEntity>) {
        deleteAllKeyResults()
        insertAllKeyResults(keyResults)
    }

    @Query("DELETE FROM key_results_table")
    fun deleteAllKeyResults()

    @Query("SELECT * FROM key_results_table WHERE key_result_id = :keyResultId")
    fun getKeyResultById(keyResultId: Long): Observable<KeyResultEntity>

    @Query("SELECT * FROM key_results_table")
    fun getAllIKeyResults(): Observable<List<KeyResultEntity>>

    @Query("DELETE FROM key_results_table WHERE key_result_goal_id =:goalId")
    fun deleteKeyResultByGoalId(goalId: Long)
}
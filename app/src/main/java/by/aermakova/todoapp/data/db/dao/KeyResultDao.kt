package by.aermakova.todoapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.aermakova.todoapp.data.db.entity.KeyResultEntity
import io.reactivex.Observable

@Dao
interface KeyResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKeyResult(keyResult: KeyResultEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllKeyResults(keyResults: List<KeyResultEntity>)

    @Query("SELECT * FROM key_results_table WHERE key_result_id = :keyResultId")
    fun getKeyResultById(keyResultId: Long): Observable<KeyResultEntity>

    @Query("SELECT * FROM key_results_table")
    fun getAllIKeyResults(): Observable<List<KeyResultEntity>>

    @Query("DELETE FROM key_results_table WHERE key_result_goal_id =:goalId")
    fun deleteKeyResultByGoalId(goalId: Long)
}
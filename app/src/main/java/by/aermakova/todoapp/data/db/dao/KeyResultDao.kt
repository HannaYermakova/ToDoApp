package by.aermakova.todoapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import by.aermakova.todoapp.data.db.entity.KeyResultEntity
import io.reactivex.Flowable

@Dao
interface KeyResultDao {

    @Insert
    fun insertKeyResult(keyResult: KeyResultEntity)

    @Insert
    fun insertAllKeyResults(keyResults: List<KeyResultEntity>)

    @Query("SELECT * FROM key_results_table WHERE keyResultId = :keyResultId")
    fun getKeyResultById(keyResultId: Long): Flowable<KeyResultEntity>

    @Query("SELECT * FROM ideas_table")
    fun getAllIKeyResults(): Flowable<List<KeyResultEntity>>
}
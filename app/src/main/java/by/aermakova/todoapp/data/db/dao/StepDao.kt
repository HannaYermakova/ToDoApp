package by.aermakova.todoapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import by.aermakova.todoapp.data.db.entity.StepEntity
import io.reactivex.Observable

@Dao
interface StepDao {

    @Insert
    fun insertStep(step: StepEntity): Long

    @Insert
    fun insertAllSteps(steps: List<StepEntity>)

    @Query("SELECT * FROM steps_table WHERE step_id = :stepId")
    fun getStepById(stepId: Long): Observable<StepEntity>

    @Query("SELECT * FROM steps_table WHERE step_key_result_id = :keyResultId")
    fun getStepByKeyResultId(keyResultId: Long): Observable<List<StepEntity>>

    @Query("SELECT * FROM steps_table")
    fun getAllSteps(): Observable<List<StepEntity>>
}
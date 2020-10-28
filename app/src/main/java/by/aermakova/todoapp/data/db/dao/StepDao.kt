package by.aermakova.todoapp.data.db.dao

import androidx.room.*
import by.aermakova.todoapp.data.db.entity.KeyResultSteps
import by.aermakova.todoapp.data.db.entity.StepEntity
import by.aermakova.todoapp.data.db.entity.StepTasks
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface StepDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStep(step: StepEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSteps(steps: List<StepEntity>)

    @Query("SELECT * FROM steps_table WHERE step_id = :stepId")
    fun getStepById(stepId: Long): Single<StepEntity>

    @Query("SELECT * FROM steps_table WHERE step_key_result_id = :keyResultId")
    fun getStepByKeyResultId(keyResultId: Long): Observable<List<StepEntity>>

    @Query("SELECT * FROM steps_table")
    fun getAllSteps(): Observable<List<StepEntity>>

    @Query("UPDATE steps_table SET step_status_done = :status WHERE step_id = :stepId")
    fun updateStatus(status: Boolean, stepId: Long)

    @Transaction
    @Query("SELECT * FROM steps_table WHERE step_id IN (:stepsId) ")
    fun getStepsWithTasksById(stepsId: List<Long>): Single<List<StepTasks>>
}
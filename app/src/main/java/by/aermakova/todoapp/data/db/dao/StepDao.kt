package by.aermakova.todoapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import by.aermakova.todoapp.data.db.entity.StepEntity
import io.reactivex.Flowable

@Dao
interface StepDao {

    @Insert
    fun insertStep(step: StepEntity)

    @Insert
    fun insertAllSteps(steps: List<StepEntity>)

    @Query("SELECT * FROM steps_table WHERE step_id = :stepId")
    fun getStepById(stepId: Long): Flowable<StepEntity>

    @Query("SELECT * FROM steps_table")
    fun getAllSteps(): Flowable<List<StepEntity>>
}
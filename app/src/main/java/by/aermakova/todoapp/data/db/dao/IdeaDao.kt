package by.aermakova.todoapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.aermakova.todoapp.data.db.entity.IdeaEntity
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface IdeaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIdea(idea: IdeaEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllIdeas(ideas: List<IdeaEntity>)

    @Query("SELECT * FROM ideas_table WHERE idea_id = :ideaId")
    fun getIdeaById(ideaId: Long): Observable<IdeaEntity>

    @Query("SELECT * FROM ideas_table")
    fun getAllIdeas(): Observable<List<IdeaEntity>>

    @Query("DELETE FROM ideas_table WHERE idea_id = :ideaId")
    fun deleteIdea(ideaId: Long)

    @Query("SELECT * FROM ideas_table WHERE idea_goal_id =:goalId")
    fun getIdeasByGoalId(goalId: Long): Single<List<IdeaEntity>>

    @Query("SELECT * FROM ideas_table WHERE idea_step_id =:stepId")
    fun getIdeasByStepId(stepId: Long): Single<List<IdeaEntity>>

    @Query("DELETE FROM ideas_table WHERE idea_goal_id =:goalId")
    fun deleteIdeaByGoalId(goalId: Long)

    @Query("SELECT idea_id FROM ideas_table WHERE idea_goal_id =:goalId")
    fun getAllIdeasIdsByGoalId(goalId: Long): Single<List<Long>>
}
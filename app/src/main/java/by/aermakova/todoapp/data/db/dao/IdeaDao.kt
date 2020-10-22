package by.aermakova.todoapp.data.db.dao

import androidx.room.*
import by.aermakova.todoapp.data.db.entity.IdeaEntity
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface IdeaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIdea(idea: IdeaEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllIdeas(ideas: List<IdeaEntity>)

    @Query("SELECT * FROM ideas_table WHERE idea_id = :ideaId")
    fun getIdeaById(ideaId: Long): Observable<IdeaEntity>

    @Query("SELECT * FROM ideas_table")
    fun getAllIdeas() : Observable<List<IdeaEntity>>

    @Query("DELETE FROM ideas_table WHERE idea_id = :ideaId")
    fun deleteIdea(ideaId: Long)
}
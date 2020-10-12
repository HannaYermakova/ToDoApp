package by.aermakova.todoapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import by.aermakova.todoapp.data.db.entity.IdeaEntity
import io.reactivex.Flowable

@Dao
interface IdeaDao {

    @Insert
    fun insertIdea(idea: IdeaEntity)

    @Insert
    fun insertAllIdeas(ideas: List<IdeaEntity>)

    @Query("SELECT * FROM ideas_table WHERE idea_id = :ideaId")
    fun getIdeaById(ideaId: Long): Flowable<IdeaEntity>

    @Query("SELECT * FROM ideas_table")
    fun getAllIdeas() : Flowable<List<IdeaEntity>>
}
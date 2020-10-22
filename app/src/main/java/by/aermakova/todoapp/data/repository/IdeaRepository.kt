package by.aermakova.todoapp.data.repository

import by.aermakova.todoapp.data.db.dao.IdeaDao
import by.aermakova.todoapp.data.db.entity.IdeaEntity
import io.reactivex.Observable
import javax.inject.Inject

class IdeaRepository @Inject constructor(
    private val ideaDao: IdeaDao
) {

    fun saveIdea(ideaEntity: IdeaEntity): Long = ideaDao.insertIdea(ideaEntity)

    fun getIdeaById(ideaId: Long): Observable<IdeaEntity> =
        ideaDao.getIdeaById(ideaId)

    fun getAllIdeas(): Observable<List<IdeaEntity>> {
        return ideaDao.getAllIdeas()
    }

    fun deleteIdea(ideaId: Long) {
        ideaDao.deleteIdea(ideaId)
    }
}
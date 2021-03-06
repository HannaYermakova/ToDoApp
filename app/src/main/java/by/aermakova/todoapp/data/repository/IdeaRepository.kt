package by.aermakova.todoapp.data.repository

import by.aermakova.todoapp.data.db.dao.IdeaDao
import by.aermakova.todoapp.data.db.entity.IdeaEntity
import io.reactivex.Single
import javax.inject.Inject

class IdeaRepository @Inject constructor(
    private val ideaDao: IdeaDao
) {

    fun saveIdea(ideaEntity: IdeaEntity): Long = ideaDao.insertIdea(ideaEntity)

    fun getIdeaById(ideaId: Long) =
        ideaDao.getIdeaById(ideaId)

    fun getAllIdeas() = ideaDao.getAllIdeas()

    fun deleteIdea(ideaId: Long) =  ideaDao.deleteIdea(ideaId) > 0

    fun saveIdeas(ideaEntities: List<IdeaEntity>) {
        ideaDao.insertAllIdeasTransaction(ideaEntities)
    }

    fun getIdeaByStepId(stepId: Long): Single<List<IdeaEntity>> {
        return ideaDao.getIdeasByStepId(stepId)
    }

    fun getAllIdeasIdByGoalId(goalId: Long): Single<List<Long>> =
        ideaDao.getAllIdeasIdsByGoalId(goalId)

    fun getAllIdeasIdByStepId(stepId: Long): Single<List<Long>> =
        ideaDao.getAllIdeasIdsByStepId(stepId)

    fun updateIdeaText(newText: String, stepId: Long)= ideaDao.updateIdeaText(newText, stepId) > 0
}
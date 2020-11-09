package by.aermakova.todoapp.data.interactor

import by.aermakova.todoapp.data.db.entity.IdeaEntity
import by.aermakova.todoapp.data.model.TextModel
import by.aermakova.todoapp.data.model.toTextModel
import by.aermakova.todoapp.data.remote.RemoteDatabase
import by.aermakova.todoapp.data.remote.model.IdeaRemoteModel
import by.aermakova.todoapp.data.remote.model.toLocal
import by.aermakova.todoapp.data.remote.model.toRemote
import by.aermakova.todoapp.data.remote.sync.RemoteSync
import by.aermakova.todoapp.data.repository.IdeaRepository
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single

class IdeaInteractor(
    private val ideaRepository: IdeaRepository,
    private val ideaRemoteDatabase: RemoteDatabase<IdeaRemoteModel>
) : RemoteSync<IdeaRemoteModel> {

    fun saveIdeaInLocalDatabase(
        text: String,
        goalId: Long,
        keyResultId: Long?,
        stepId: Long?
    ): Long {
        return ideaRepository.saveIdea(
            IdeaEntity(
                text = text,
                ideaGoalId = goalId,
                ideaKeyResultId = keyResultId,
                ideaStepId = stepId
            )
        )
    }

    fun getIdeaById(ideaId: Long): Observable<IdeaEntity> = ideaRepository.getIdeaById(ideaId)

    fun saveIdeaToRemote(ideaEntity: IdeaEntity?) {
        ideaEntity?.let {
            ideaRemoteDatabase.saveData(it.toRemote())
        }
    }

    fun getAllIdeas(): Observable<List<IdeaEntity>> {
        return ideaRepository.getAllIdeas()
    }

    fun deleteIdea(ideaId: Long) {
        ideaRepository.deleteIdea(ideaId)
        ideaRemoteDatabase.removeData(ideaId.toString())
    }

    override fun addItemsDataListener(dataObserver: Observer<List<IdeaRemoteModel>>) {
        ideaRemoteDatabase.addDataListener(dataObserver)
    }

    override fun saveItemsInLocalDatabase(list: List<IdeaRemoteModel>) {
        ideaRepository.saveIdeas(list.map { it.toLocal() })
    }

    fun getIdeasByStepId(stepId: Long): Single<List<TextModel>> {
       return ideaRepository.getIdeaByStepId(stepId).map { list -> list.map { it.toTextModel() } }
    }
}
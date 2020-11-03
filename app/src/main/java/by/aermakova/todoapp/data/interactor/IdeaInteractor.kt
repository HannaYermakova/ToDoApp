package by.aermakova.todoapp.data.interactor

import by.aermakova.todoapp.data.db.entity.IdeaEntity
import by.aermakova.todoapp.data.remote.RemoteDatabase
import by.aermakova.todoapp.data.remote.model.IdeaRemoteModel
import by.aermakova.todoapp.data.remote.model.toLocal
import by.aermakova.todoapp.data.remote.model.toRemote
import by.aermakova.todoapp.data.remote.sync.RemoteSync
import by.aermakova.todoapp.data.repository.IdeaRepository
import io.reactivex.Observable
import io.reactivex.Observer

class IdeaInteractor(
    private val ideaRepository: IdeaRepository,
    private val ideaRemoteDatabase: RemoteDatabase<IdeaRemoteModel>
) : RemoteSync<IdeaRemoteModel> {

    fun saveIdeaInLocalDatabase(
        text: String,
        goalId: Long,
        keyResultId: Long
    ): Long {
        return ideaRepository.saveIdea(
            IdeaEntity(
                text = text,
                ideaGoalId = goalId,
                ideaKeyResultId = keyResultId
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

    fun addIdeasDataListener(observer: Observer<List<IdeaRemoteModel>>) {
        ideaRemoteDatabase.addDataListener(observer)
    }

    fun saveIdeasInLocalDatabase(list: List<IdeaRemoteModel>) {
        ideaRepository.saveIdeas(list.map { it.toLocal() })
    }

    override fun addItemsDataListener(dataObserver: Observer<List<IdeaRemoteModel>>) {
        ideaRemoteDatabase.addDataListener(dataObserver)
    }

    override fun saveItemsInLocalDatabase(list: List<IdeaRemoteModel>) {
        ideaRepository.saveIdeas(list.map { it.toLocal() })
    }
}
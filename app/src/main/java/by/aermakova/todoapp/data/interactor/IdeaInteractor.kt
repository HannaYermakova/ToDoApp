package by.aermakova.todoapp.data.interactor

import by.aermakova.todoapp.data.db.entity.IdeaEntity
import by.aermakova.todoapp.data.remote.DeleteGoalItems
import by.aermakova.todoapp.data.remote.DeleteStepItems
import by.aermakova.todoapp.data.remote.RemoteDatabase
import by.aermakova.todoapp.data.remote.model.IdeaRemoteModel
import by.aermakova.todoapp.data.remote.model.toLocal
import by.aermakova.todoapp.data.remote.model.toRemote
import by.aermakova.todoapp.data.remote.sync.RemoteSync
import by.aermakova.todoapp.data.repository.IdeaRepository
import by.aermakova.todoapp.data.useCase.editText.ChangeItemText
import io.reactivex.Observer
import io.reactivex.Single

class IdeaInteractor(
    private val ideaRepository: IdeaRepository,
    private val ideaRemoteDatabase: RemoteDatabase<IdeaRemoteModel>
) : RemoteSync<IdeaRemoteModel>, DeleteGoalItems, DeleteStepItems, ChangeItemText<IdeaEntity> {

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

    fun getIdeaById(ideaId: Long) = ideaRepository.getIdeaById(ideaId)

    fun saveIdeaToRemote(ideaEntity: IdeaEntity?) {
        ideaEntity?.let {
            ideaRemoteDatabase.saveData(it.toRemote())
        }
    }

    fun getAllIdeas() = ideaRepository.getAllIdeas()

    fun deleteIdea(ideaId: Long) {
        ideaRepository.deleteIdea(ideaId)
        ideaRemoteDatabase.removeData(ideaId)
    }

    override fun addItemsDataListener(dataObserver: Observer<List<IdeaRemoteModel>>) {
        ideaRemoteDatabase.addDataListener(dataObserver)
    }

    override fun saveItemsInLocalDatabase(list: List<IdeaRemoteModel>) {
        ideaRepository.saveIdeas(list.map { it.toLocal() })
    }

    fun getIdeasByStepId(stepId: Long) = ideaRepository.getIdeaByStepId(stepId)

    override fun deleteGoalsItemsById(goalId: Long) =
        ideaRepository.getAllIdeasIdByGoalId(goalId).map { ids ->
            ids.map { ideaRemoteDatabase.removeData(it) }
        }

    override fun deleteStepItemsById(stepId: Long) =
        ideaRepository.getAllIdeasIdByStepId(stepId).map { ids ->
            ids.map { ideaRemoteDatabase.removeData(it) }
        }

    fun deleteIdeaByIdRemote(itemId: Long): Single<Boolean> {
        ideaRemoteDatabase.removeData(itemId)
        return Single.just(true)
    }

    fun deleteIdeaByIdLocal(itemId: Long) = Single.just(ideaRepository.deleteIdea(itemId))

    override fun updateItemTextLocal(newText: String, itemId: Long) =
        ideaRepository.updateIdeaText(newText, itemId)

    override fun updateItemToRemote(entity: IdeaEntity?) {
        entity?.let { ideaRemoteDatabase.updateData(it.toRemote()) }
    }

    override fun getItemById(itemId: Long) = getIdeaById(itemId)
}
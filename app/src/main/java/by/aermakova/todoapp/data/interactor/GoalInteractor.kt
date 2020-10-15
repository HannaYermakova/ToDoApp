package by.aermakova.todoapp.data.interactor

import by.aermakova.todoapp.data.db.entity.GoalEntity
import by.aermakova.todoapp.data.db.entity.KeyResultEntity
import by.aermakova.todoapp.data.db.entity.toModel
import by.aermakova.todoapp.data.model.Goal
import by.aermakova.todoapp.data.repository.GoalRepository
import io.reactivex.Observable

class GoalInteractor (
    private val goalRepository: GoalRepository
) {
    fun saveGoal(goalTitle: String, keyResults: List<String>) {
        val goalId = goalRepository.saveGoalInLocalDataBase(
            GoalEntity(
                goalStatusDone = false,
                text = goalTitle
            )
        )
        goalRepository.saveKeyResults(keyResults.map { text ->
            KeyResultEntity(
                keyResultGoalId = goalId,
                text = text
            )
        })
    }

    fun getAllGoalsWithKeyResults() : Observable<List<Goal>>{
        return goalRepository.getAllGoalsWithKeyResults().map { list -> list.map { it.toModel() } }
    }
}
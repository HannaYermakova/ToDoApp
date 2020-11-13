package by.aermakova.todoapp.data.remote

import io.reactivex.Single


interface DeleteGoalItems {

    fun deleteGoalsItemsById(goalId: Long): Single<List<Unit>>

    fun deleteGoalByIdRemote(goalId: Long) {}
}
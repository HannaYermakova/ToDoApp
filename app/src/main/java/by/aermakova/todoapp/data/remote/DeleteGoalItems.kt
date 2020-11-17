package by.aermakova.todoapp.data.remote

import io.reactivex.Single


interface DeleteGoalItems {

    fun deleteGoalsItemsById(goalId: Long): Single<List<Unit>>
}

interface DeleteStepItems {

    fun deleteStepItemsById(stepId: Long): Single<List<Unit>>
}

interface DeleteTaskItems {

    fun deleteTaskItemsById(stepId: Long): Single<List<Unit>>
}
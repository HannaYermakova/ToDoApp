package by.aermakova.todoapp.data.di.module

import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.repository.GoalRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class InteractorModule {

    @Provides
    fun provideGoalInteractor(goalRepository: GoalRepository) : GoalInteractor
            = GoalInteractor(goalRepository)
}
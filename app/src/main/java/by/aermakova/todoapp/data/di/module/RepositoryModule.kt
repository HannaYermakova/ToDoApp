package by.aermakova.todoapp.data.di.module

import by.aermakova.todoapp.data.db.dao.GoalDao
import by.aermakova.todoapp.data.db.dao.KeyResultDao
import by.aermakova.todoapp.data.repository.GoalRepository
import dagger.Module
import dagger.Provides

@Module(includes = [InteractorModule::class])
class RepositoryModule {

    @Provides
    fun provideGoalRepo(goalDao: GoalDao, keyResultDao: KeyResultDao) : GoalRepository =
        GoalRepository(goalDao, keyResultDao)
}
package by.aermakova.todoapp.data.di.module

import by.aermakova.todoapp.data.db.dao.GoalDao
import by.aermakova.todoapp.data.db.dao.KeyResultDao
import by.aermakova.todoapp.data.db.dao.StepDao
import by.aermakova.todoapp.data.repository.GoalRepository
import by.aermakova.todoapp.data.repository.StepRepository
import dagger.Module
import dagger.Provides

@Module(includes = [InteractorModule::class])
class RepositoryModule {

    @Provides
    fun provideGoalRepo(goalDao: GoalDao, keyResultDao: KeyResultDao): GoalRepository =
        GoalRepository(goalDao, keyResultDao)

    @Provides
    fun provideStepRepo(stepDao: StepDao): StepRepository =
        StepRepository(stepDao)
}
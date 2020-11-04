package by.aermakova.todoapp.data.di.module

import by.aermakova.todoapp.data.db.dao.GoalDao
import by.aermakova.todoapp.data.db.dao.KeyResultDao
import by.aermakova.todoapp.data.db.dao.StepDao
import by.aermakova.todoapp.data.db.dao.TaskDao
import by.aermakova.todoapp.data.di.scope.ApplicationScope
import by.aermakova.todoapp.data.repository.GoalRepository
import by.aermakova.todoapp.data.repository.StepRepository
import by.aermakova.todoapp.data.repository.TaskRepository
import dagger.Module
import dagger.Provides

@Module(includes = [InteractorModule::class])
class RepositoryModule {

    @ApplicationScope
    @Provides
    fun provideGoalRepo(
        goalDao: GoalDao,
        keyResultDao: KeyResultDao,
        taskDao: TaskDao,
        stepDao: StepDao
    ): GoalRepository =
        GoalRepository(goalDao, keyResultDao, stepDao, taskDao)

    @ApplicationScope
    @Provides
    fun provideStepRepo(stepDao: StepDao): StepRepository =
        StepRepository(stepDao)

    @ApplicationScope
    @Provides
    fun provideTaskRepo(taskDao: TaskDao): TaskRepository =
        TaskRepository(taskDao)
}
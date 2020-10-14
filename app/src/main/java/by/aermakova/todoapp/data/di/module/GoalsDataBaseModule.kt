package by.aermakova.todoapp.data.di.module

import android.content.Context
import androidx.room.Room
import by.aermakova.todoapp.data.db.database.GOALS_DATABASE_NAME
import by.aermakova.todoapp.data.db.database.GoalsDataBase
import by.aermakova.todoapp.data.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RepositoryModule::class])
class GoalsDataBaseModule {

    @Provides
    fun provideDataBase(appContext: Context) :GoalsDataBase =
        Room.databaseBuilder(appContext, GoalsDataBase::class.java, GOALS_DATABASE_NAME).build()

    @Provides
    fun provideTaskDao(dataBase: GoalsDataBase) = dataBase.getTaskDao()

    @Provides
    fun provideGoalDao(dataBase: GoalsDataBase) = dataBase.getGoalDao()

    @Provides
    fun provideIdeaDao(dataBase: GoalsDataBase) = dataBase.getIdeaDao()

    @Provides
    fun provideStepDao(dataBase: GoalsDataBase) = dataBase.getStepDao()

    @Provides
    fun provideKeyResultDao(dataBase: GoalsDataBase) = dataBase.getKeyResultDao()
}
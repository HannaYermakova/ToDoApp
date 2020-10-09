package by.aermakova.todoapp.data.di.module

import android.content.Context
import androidx.room.Room
import by.aermakova.todoapp.data.db.database.GOALS_DATABASE_NAME
import by.aermakova.todoapp.data.db.database.GoalsDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class GoalsDataBaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@Named("application_context") appContext: Context) =
        Room.databaseBuilder(appContext, GoalsDataBase::class.java, GOALS_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideTaskDao(dataBase: GoalsDataBase) = dataBase.getTaskDao()

    @Singleton
    @Provides
    fun provideGoalDao(dataBase: GoalsDataBase) = dataBase.getGoalDao()

    @Singleton
    @Provides
    fun provideIdeaDao(dataBase: GoalsDataBase) = dataBase.getIdeaDao()

    @Singleton
    @Provides
    fun provideStepDao(dataBase: GoalsDataBase) = dataBase.getStepDao()

    @Singleton
    @Provides
    fun provideKeyResultDao(dataBase: GoalsDataBase) = dataBase.getKeyResultDao()
}
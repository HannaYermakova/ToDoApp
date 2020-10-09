package by.aermakova.todoapp.data.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ActivityModule(private val context: Context) {

    @Named("activity_context")
    @Provides
    fun provideContext(): Context = context
}
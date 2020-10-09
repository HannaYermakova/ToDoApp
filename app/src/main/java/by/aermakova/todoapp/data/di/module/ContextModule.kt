package by.aermakova.todoapp.data.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ContextModule(private val context: Context) {

    @Named("application_context")
    @Provides
    fun provideContext(): Context =  context.applicationContext

}
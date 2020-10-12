package by.aermakova.todoapp.data.di.module

import by.aermakova.todoapp.ui.app.App
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val app: App) {

    @Provides
    fun provideApp(): App = app
}
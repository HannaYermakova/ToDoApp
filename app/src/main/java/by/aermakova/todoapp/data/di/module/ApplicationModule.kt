package by.aermakova.todoapp.data.di.module

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.aermakova.todoapp.data.di.CustomViewModelFactory
import by.aermakova.todoapp.ui.app.App
import dagger.MapKey
import dagger.Module
import dagger.Provides
import kotlin.reflect.KClass

@Module(includes = [GoalsDataBaseModule::class])
class ApplicationModule(private val app: App) {

    @Provides
    fun provideApp(): App = app

    @Provides
    fun provideApplicationContext(): Context = app.applicationContext

    @Provides
    fun providesViewModelFactory(factory: CustomViewModelFactory): ViewModelProvider.Factory =
        factory
}

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(
    val value: KClass<out ViewModel>
)
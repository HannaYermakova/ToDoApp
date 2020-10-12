package by.aermakova.todoapp.data.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.aermakova.todoapp.data.di.CustomViewModelFactory
import by.aermakova.todoapp.ui.app.App
import dagger.MapKey
import dagger.Module
import dagger.Provides
import kotlin.reflect.KClass

@Module
class ApplicationModule(private val app: App) {

    @Provides
    fun provideApp(): App = app

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
package by.aermakova.todoapp.data.di.module

import android.app.Activity
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [AndroidSupportInjectionModule::class])
open class ActivityModule(private val activity: Activity) {

    @Provides
    fun provideActivity(): Activity = activity
}
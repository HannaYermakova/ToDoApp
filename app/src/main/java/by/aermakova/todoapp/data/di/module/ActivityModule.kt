package by.aermakova.todoapp.data.di.module

import android.app.Activity
import by.aermakova.todoapp.ui.app.AppActivity
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [AndroidSupportInjectionModule::class])
open class ActivityModule(private val activity: AppActivity) {

    @Provides
    fun provideActivity() : Activity = activity
}
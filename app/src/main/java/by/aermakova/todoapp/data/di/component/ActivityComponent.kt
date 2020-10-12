package by.aermakova.todoapp.data.di.component

import android.app.Activity
import by.aermakova.todoapp.data.di.module.ActivityModule
import by.aermakova.todoapp.ui.app.App
import dagger.Subcomponent
import dagger.android.AndroidInjectionModule
import dagger.android.DispatchingAndroidInjector

@Subcomponent(modules = [AndroidInjectionModule::class, ActivityModule::class])
interface ActivityComponent {

    val activityInjector: DispatchingAndroidInjector<Activity>

    fun inject(application: App)
}
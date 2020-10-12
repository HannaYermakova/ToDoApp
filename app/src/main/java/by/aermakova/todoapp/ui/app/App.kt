package by.aermakova.todoapp.ui.app

import android.app.Application
import by.aermakova.todoapp.data.di.component.ApplicationComponent
import by.aermakova.todoapp.data.di.component.DaggerApplicationComponent
import by.aermakova.todoapp.data.di.module.ActivityModule
import by.aermakova.todoapp.data.di.module.ApplicationModule

import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

class App : Application() {

    private lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()

        applicationComponent.activityComponent.inject(this)

        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
    }

    fun tryInjectActivity(activity: AppActivity): Boolean {
        return applicationComponent
            .getActivityComponent(ActivityModule(activity))
            .activityInjector
            .maybeInject(activity)
    }
}
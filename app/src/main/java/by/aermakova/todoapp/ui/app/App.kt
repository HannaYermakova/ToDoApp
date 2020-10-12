package by.aermakova.todoapp.ui.app

import android.app.Activity
import android.app.Application
import by.aermakova.todoapp.data.di.component.ApplicationComponent
import by.aermakova.todoapp.data.di.component.DaggerApplicationComponent
import by.aermakova.todoapp.data.di.module.ContextModule
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

class App : Application() {

    private lateinit var appComponent: ApplicationComponent

    companion object {
        fun get(activity: Activity): App {
            return activity.application as App
        }
    }

    fun getAppComponent() = appComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
            .contextModule(ContextModule(this))
            .build()

        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
    }
}
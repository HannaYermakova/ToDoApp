package by.aermakova.todoapp.data.di.component

import by.aermakova.todoapp.data.di.module.ActivityModule
import by.aermakova.todoapp.ui.app.App
import dagger.Subcomponent

@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(application: App)
}
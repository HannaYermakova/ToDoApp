package by.aermakova.todoapp.data.di.component

import by.aermakova.todoapp.data.di.module.ApplicationModule
import by.aermakova.todoapp.data.di.module.ContextModule
import dagger.Component

@Component(modules = [ApplicationModule::class, ContextModule::class])
interface ApplicationComponent {

    val activityComponent : ActivityComponent
}
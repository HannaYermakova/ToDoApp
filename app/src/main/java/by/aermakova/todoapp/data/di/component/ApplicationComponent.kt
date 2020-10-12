package by.aermakova.todoapp.data.di.component

import by.aermakova.todoapp.data.di.module.ActivityModule
import by.aermakova.todoapp.data.di.module.ApplicationModule
import by.aermakova.todoapp.data.di.module.ContextModule
import by.aermakova.todoapp.data.di.module.FragmentModule
import by.aermakova.todoapp.data.di.scope.ActivityScope
import by.aermakova.todoapp.data.di.scope.ApplicationScope
import dagger.Component
import dagger.Subcomponent

@ApplicationScope
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    val activityComponent: ActivityComponent

    fun getActivityComponent(activityModule: ActivityModule) : LocalActivityComponent

    @ActivityScope
    @Subcomponent(modules = [ActivityModule::class, FragmentModule::class])
    interface LocalActivityComponent : ActivityComponent
}
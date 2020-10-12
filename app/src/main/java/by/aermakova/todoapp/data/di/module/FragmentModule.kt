package by.aermakova.todoapp.data.di.module

import by.aermakova.todoapp.ui.app.AppActivity
import by.aermakova.todoapp.ui.app.AppModule
import by.aermakova.todoapp.ui.goal.GoalsModule
import by.aermakova.todoapp.ui.goal.GoalsFragment
import by.aermakova.todoapp.ui.login.LoginFragment
import by.aermakova.todoapp.ui.login.LoginModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector(modules = [AppModule::class])
    abstract fun provideAppActivity(): AppActivity

    @ContributesAndroidInjector(modules = [GoalsModule::class])
    abstract fun provideGoalsFragment(): GoalsFragment

    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun provideLoginFragment(): LoginFragment
}
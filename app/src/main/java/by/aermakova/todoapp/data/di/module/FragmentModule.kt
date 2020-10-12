package by.aermakova.todoapp.data.di.module

import by.aermakova.todoapp.ui.app.AppActivity
import by.aermakova.todoapp.ui.goal.GoalFragmentModule
import by.aermakova.todoapp.ui.goal.GoalsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector(modules = [GoalFragmentModule::class])
    abstract fun provideAppActivity(): AppActivity

    @ContributesAndroidInjector(modules = [GoalFragmentModule::class])
    abstract fun provideGoalsFragment(): GoalsFragment
}
package by.aermakova.todoapp.data.di.module

import by.aermakova.todoapp.ui.app.AppActivity
import by.aermakova.todoapp.ui.app.AppModule
import by.aermakova.todoapp.ui.auth.AuthFlowFragment
import by.aermakova.todoapp.ui.auth.AuthFlowModule
import by.aermakova.todoapp.ui.goal.AddGoalFragment
import by.aermakova.todoapp.ui.goal.AddGoalModule
import by.aermakova.todoapp.ui.goal.GoalsModule
import by.aermakova.todoapp.ui.goal.GoalsFragment
import by.aermakova.todoapp.ui.login.LoginFragment
import by.aermakova.todoapp.ui.login.LoginModule
import by.aermakova.todoapp.ui.main.MainFlowFragment
import by.aermakova.todoapp.ui.main.MainFlowModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector(modules = [AppModule::class])
    abstract fun provideAppActivity(): AppActivity

    @ContributesAndroidInjector(modules = [MainFlowModule::class])
    abstract fun provideMainFlowFragment(): MainFlowFragment

    @ContributesAndroidInjector(modules = [AuthFlowModule::class])
    abstract fun provideAuthFlowFragment(): AuthFlowFragment

    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun provideLoginFragment(): LoginFragment

    @ContributesAndroidInjector(modules = [GoalsModule::class])
    abstract fun provideGoalsFragment(): GoalsFragment

    @ContributesAndroidInjector(modules = [AddGoalModule::class])
    abstract fun provideAddGoalFragment(): AddGoalFragment
}
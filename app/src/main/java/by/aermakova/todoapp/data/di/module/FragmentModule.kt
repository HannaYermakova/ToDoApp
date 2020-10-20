package by.aermakova.todoapp.data.di.module

import by.aermakova.todoapp.ui.app.AppActivity
import by.aermakova.todoapp.ui.app.AppModule
import by.aermakova.todoapp.ui.auth.AuthFlowFragment
import by.aermakova.todoapp.ui.auth.AuthFlowModule
import by.aermakova.todoapp.ui.dialog.addItem.AddItemModule
import by.aermakova.todoapp.ui.dialog.addItem.AddItemDialogFragment
import by.aermakova.todoapp.ui.dialog.selectItem.SelectItemDialogFragment
import by.aermakova.todoapp.ui.dialog.selectItem.SelectItemModule
import by.aermakova.todoapp.ui.dialog.selectItem.goal.SelectGoalDialogFragment
import by.aermakova.todoapp.ui.goal.AddGoalFragment
import by.aermakova.todoapp.ui.goal.AddGoalModule
import by.aermakova.todoapp.ui.goal.GoalsFragment
import by.aermakova.todoapp.ui.goal.GoalsModule
import by.aermakova.todoapp.ui.goal.details.GoalDetailsFragment
import by.aermakova.todoapp.ui.goal.details.GoalDetailsModule
import by.aermakova.todoapp.ui.login.LoginFragment
import by.aermakova.todoapp.ui.login.LoginModule
import by.aermakova.todoapp.ui.main.MainFlowFragment
import by.aermakova.todoapp.ui.main.MainFlowModule
import by.aermakova.todoapp.ui.task.TasksFragment
import by.aermakova.todoapp.ui.task.TasksModule
import by.aermakova.todoapp.ui.task.addNew.AddTaskFragment
import by.aermakova.todoapp.ui.task.addNew.AddTaskModule
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

    @ContributesAndroidInjector(modules = [GoalDetailsModule::class])
    abstract fun provideGoalDetailsFragment(): GoalDetailsFragment

    @ContributesAndroidInjector(modules = [AddItemModule::class])
    abstract fun provideAddItemDialogFragment(): AddItemDialogFragment

    @ContributesAndroidInjector(modules = [TasksModule::class])
    abstract fun provideTasksFragment(): TasksFragment

    @ContributesAndroidInjector(modules = [AddTaskModule::class])
    abstract fun provideAddTaskFragment(): AddTaskFragment

    @ContributesAndroidInjector(modules = [SelectItemModule::class])
    abstract fun provideSelectGoalDialogFragment(): SelectGoalDialogFragment
}
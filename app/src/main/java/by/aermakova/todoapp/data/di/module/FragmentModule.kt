package by.aermakova.todoapp.data.di.module

import by.aermakova.todoapp.ui.app.AppActivity
import by.aermakova.todoapp.ui.app.AppModule
import by.aermakova.todoapp.ui.auth.AuthFlowFragment
import by.aermakova.todoapp.ui.auth.AuthFlowModule
import by.aermakova.todoapp.ui.dialog.addItem.AddItemDialogFragment
import by.aermakova.todoapp.ui.dialog.addItem.AddItemModule
import by.aermakova.todoapp.ui.dialog.datePicker.PickDayDialogFragment
import by.aermakova.todoapp.ui.dialog.datePicker.PickDayModule
import by.aermakova.todoapp.ui.dialog.selectItem.SelectItemModule
import by.aermakova.todoapp.ui.dialog.selectItem.goal.SelectGoalDialogFragment
import by.aermakova.todoapp.ui.dialog.selectItem.keyResult.SelectKeyResultDialogFragment
import by.aermakova.todoapp.ui.dialog.selectItem.step.SelectStepDialogFragment
import by.aermakova.todoapp.ui.goal.addNew.AddGoalFragment
import by.aermakova.todoapp.ui.goal.addNew.AddGoalModule
import by.aermakova.todoapp.ui.goal.details.GoalDetailsFragment
import by.aermakova.todoapp.ui.goal.details.GoalDetailsModule
import by.aermakova.todoapp.ui.goal.main.GoalsFragment
import by.aermakova.todoapp.ui.goal.main.GoalsModule
import by.aermakova.todoapp.ui.idea.addNew.AddIdeaFragment
import by.aermakova.todoapp.ui.idea.addNew.AddIdeaModule
import by.aermakova.todoapp.ui.idea.main.IdeaFragment
import by.aermakova.todoapp.ui.idea.main.IdeaModule
import by.aermakova.todoapp.ui.login.LoginFragment
import by.aermakova.todoapp.ui.login.LoginModule
import by.aermakova.todoapp.ui.main.MainFlowFragment
import by.aermakova.todoapp.ui.main.MainFlowModule
import by.aermakova.todoapp.ui.step.addNew.AddStepFragment
import by.aermakova.todoapp.ui.step.addNew.AddStepModule
import by.aermakova.todoapp.ui.step.main.StepsFragment
import by.aermakova.todoapp.ui.step.main.StepsModule
import by.aermakova.todoapp.ui.task.addNew.AddTaskFragment
import by.aermakova.todoapp.ui.task.addNew.AddTaskModule
import by.aermakova.todoapp.ui.task.details.TaskDetailsFragment
import by.aermakova.todoapp.ui.task.details.TaskDetailsModule
import by.aermakova.todoapp.ui.task.main.TasksFragment
import by.aermakova.todoapp.ui.task.main.TasksModule
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

    @ContributesAndroidInjector(modules = [SelectItemModule::class])
    abstract fun provideSelectKeyResultDialogFragment(): SelectKeyResultDialogFragment

    @ContributesAndroidInjector(modules = [SelectItemModule::class])
    abstract fun provideSelectStepDialogFragment(): SelectStepDialogFragment

    @ContributesAndroidInjector(modules = [PickDayModule::class])
    abstract fun providePickDayDialogFragment(): PickDayDialogFragment

    @ContributesAndroidInjector(modules = [TaskDetailsModule::class])
    abstract fun provideTaskDetailsFragment(): TaskDetailsFragment

    @ContributesAndroidInjector(modules = [StepsModule::class])
    abstract fun provideStepsFragment(): StepsFragment

    @ContributesAndroidInjector(modules = [AddStepModule::class])
    abstract fun provideAddStepFragment(): AddStepFragment

    @ContributesAndroidInjector(modules = [IdeaModule::class])
    abstract fun provideIdeaFragment(): IdeaFragment

    @ContributesAndroidInjector(modules = [AddIdeaModule::class])
    abstract fun provideAddIdeaFragment(): AddIdeaFragment
}
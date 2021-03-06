package by.aermakova.todoapp.ui.task.main

import android.app.Activity
import android.content.res.Resources
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.di.scope.*
import by.aermakova.todoapp.data.interactor.TaskInteractor
import by.aermakova.todoapp.data.useCase.delete.DeleteTaskUseCase
import by.aermakova.todoapp.data.useCase.FindTaskUseCase
import by.aermakova.todoapp.data.useCase.bottomMenu.TaskBottomSheetMenuUseCase
import by.aermakova.todoapp.data.useCase.actionEnum.TasksActionItem
import by.aermakova.todoapp.databinding.BottomSheetFilterTaskBinding
import by.aermakova.todoapp.databinding.BottomSheetSortTaskBinding
import by.aermakova.todoapp.databinding.BottomSheetTaskActionBinding
import by.aermakova.todoapp.ui.dialog.confirm.ConfirmDialogNavigation
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.ui.task.TasksNavigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap


@Module
class TasksModule {

    @Provides
    fun provideTaskBottomSheetMenuUseCase(
        deleteTaskUseCase: DeleteTaskUseCase,
        @TaskActionMenu taskActionBind: BottomSheetTaskActionBinding,
        dialog: BottomSheetDialog,
        taskActionItems: Array<TasksActionItem>,
        resources: Resources,
        @NavigationTasks mainFlowNavigation: TasksNavigation,
        findTaskUseCase: FindTaskUseCase
    ) = TaskBottomSheetMenuUseCase(
        deleteTaskUseCase,
        taskActionBind,
        mainFlowNavigation,
        findTaskUseCase,
        dialog,
        taskActionItems,
        resources
    )

    @Provides
    fun provideFindTaskUseCase(taskInteractor: TaskInteractor) =
        FindTaskUseCase(taskInteractor)

    @Provides
    fun provideDeleteTaskUseCase(
        taskInteractor: TaskInteractor,
        @ErrorDeleteTask errorMessage: String,
        @DialogConfirm dialogNavigation: DialogNavigation<Boolean>,
        @TitleDialogDeleteTask dialogTitle: String
    ) = DeleteTaskUseCase(
        taskInteractor,
        errorMessage,
        dialogNavigation,
        dialogTitle
    )

    @Provides
    @DialogConfirm
    fun provideDialogNavigation(controller: NavController, @TitleDialogDeleteTask dialogTitle: String): DialogNavigation<Boolean> =
        ConfirmDialogNavigation(controller, dialogTitle)

    @Provides
    @ErrorDeleteTask
    fun provideErrorDeleteTaskMessage(activity: Activity) =
        activity.getString(R.string.error_delete_task)

    @Provides
    @TitleDialogDeleteTask
    fun provideTitleDialogDeleteTask(activity: Activity) =
        activity.getString(R.string.confirm_delete_task)

    @Provides
    @TaskActionMenu
    fun provideBottomSheetTaskActionBinding(fragment: TasksFragment): BottomSheetTaskActionBinding {
        val bind: BottomSheetTaskActionBinding = DataBindingUtil.inflate(
            fragment.layoutInflater,
            R.layout.bottom_sheet_task_action,
            null,
            false
        )
        bind.lifecycleOwner = fragment
        return bind
    }

    @Provides
    fun provideTasksActionItem(): Array<TasksActionItem> = TasksActionItem.values()

    @Provides
    @FilterTaskMenu
    fun provideFilterBottomSheetBinding(fragment: TasksFragment): BottomSheetFilterTaskBinding {
        val bind: BottomSheetFilterTaskBinding = DataBindingUtil.inflate(
            fragment.layoutInflater,
            R.layout.bottom_sheet_filter_task,
            null,
            false
        )
        bind.lifecycleOwner = fragment
        return bind
    }

    @Provides
    @SortTaskMenu
    fun provideSortBottomSheetBinding(fragment: TasksFragment): BottomSheetSortTaskBinding {
        val bind: BottomSheetSortTaskBinding = DataBindingUtil.inflate(
            fragment.layoutInflater,
            R.layout.bottom_sheet_sort_task,
            null,
            false
        )
        bind.lifecycleOwner = fragment
        return bind
    }

    @Provides
    fun provideBottomSheetDialog(activity: Activity): BottomSheetDialog =
        BottomSheetDialog(activity)

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    fun provideResources(activity: Activity): Resources =
        activity.resources

    @Provides
    @NavigationTasks
    fun provideTasksNavigation(controller: NavController) =
        TasksNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(TasksViewModel::class)
    fun provideViewModel(viewModel: TasksViewModel): TasksViewModel = viewModel
}
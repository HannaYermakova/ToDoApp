package by.aermakova.todoapp.ui.idea.main

import android.app.Activity
import android.content.res.Resources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.data.di.scope.*
import by.aermakova.todoapp.data.interactor.IdeaInteractor
import by.aermakova.todoapp.data.useCase.*
import by.aermakova.todoapp.data.useCase.actionEnum.IdeasActionItem
import by.aermakova.todoapp.data.useCase.bottomMenu.IdeaBottomSheetMenuUseCase
import by.aermakova.todoapp.databinding.BottomSheetIdeaActionBinding
import by.aermakova.todoapp.ui.dialog.confirm.ConfirmDialogNavigation
import by.aermakova.todoapp.ui.idea.IdeasNavigation
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class IdeaModule {

    @Provides
    fun provideTaskBottomSheetMenuUseCase(
        deleteTaskUseCase: DeleteIdeaUseCase,
        taskActionBind: BottomSheetIdeaActionBinding,
        dialog: BottomSheetDialog,
        taskActionItems: Array<IdeasActionItem>,
        resources: Resources,
        @NavigationIdeas mainFlowNavigation: MainFlowNavigation,
        findTaskUseCase: FindIdeaUseCase
    ) = IdeaBottomSheetMenuUseCase(
        deleteTaskUseCase,
        taskActionBind,
        mainFlowNavigation,
        findTaskUseCase,
        dialog,
        taskActionItems,
        resources
    )

    @Provides
    fun provideIdeasActionItem(): Array<IdeasActionItem> = IdeasActionItem.values()

    @Provides
    fun provideBottomSheetTaskActionBinding(fragment: IdeaFragment): BottomSheetIdeaActionBinding {
        val bind: BottomSheetIdeaActionBinding = DataBindingUtil.inflate(
            fragment.layoutInflater,
            R.layout.bottom_sheet_idea_action,
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
    fun provideResources(activity: Activity): Resources =
        activity.resources

    @Provides
    @DialogConfirm
    fun provideDialogNavigation(controller: NavController): DialogNavigation<Boolean> =
        ConfirmDialogNavigation(controller)

    @Provides
    fun provideFindIdeaUseCase(ideaInteractor: IdeaInteractor) =
        FindIdeaUseCase(ideaInteractor)

    @Provides
    fun provideDeleteIdeaUseCase(
        ideaInteractor: IdeaInteractor,
        @ErrorDeleteIdea errorMessage: String,
        @DialogConfirm dialogNavigation: DialogNavigation<Boolean>,
        @TitleDialogDeleteIdea dialogTitle: String
    ) = DeleteIdeaUseCase(
        ideaInteractor,
        errorMessage,
        dialogNavigation,
        dialogTitle
    )

    @Provides
    fun provideLoadAllIdeasUseCase(
        ideaInteractor: IdeaInteractor,
        @ErrorWhileLoading errorMessage: String
    ) =
        LoadAllIdeasUseCase(ideaInteractor, errorMessage)

    @Provides
    @ErrorWhileLoading
    fun provideErrorMessage(activity: Activity) =
        activity.getString(R.string.error_while_loading)

    @Provides
    @TitleDialogDeleteIdea
    fun provideTitleDialogDeleteIdea(activity: Activity) =
        activity.getString(R.string.confirm_delete_idea)

    @Provides
    @ErrorDeleteIdea
    fun provideErrorDeleteIdea(activity: Activity) =
        activity.getString(R.string.error_while_loading)

    @Provides
    fun provideNavController(activity: Activity): NavController =
        Navigation.findNavController(activity, R.id.app_host_fragment)

    @Provides
    @NavigationIdeas
    fun provideIdeasNavigation(controller: NavController): MainFlowNavigation =
        IdeasNavigation(controller)

    @Provides
    @IntoMap
    @ViewModelKey(IdeaViewModel::class)
    fun provideViewModel(viewModel: IdeaViewModel): ViewModel = viewModel
}
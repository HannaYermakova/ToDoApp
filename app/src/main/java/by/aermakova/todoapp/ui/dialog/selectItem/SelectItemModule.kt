package by.aermakova.todoapp.ui.dialog.selectItem

import androidx.lifecycle.ViewModel
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.ui.dialog.selectItem.goal.SelectGoalViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class SelectItemModule {

    @Provides
    @IntoMap
    @ViewModelKey(SelectGoalViewModel::class)
    fun provideViewModel(viewModel: SelectGoalViewModel) : ViewModel = viewModel
}
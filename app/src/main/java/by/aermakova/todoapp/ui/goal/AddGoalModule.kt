package by.aermakova.todoapp.ui.goal

import androidx.lifecycle.ViewModel
import by.aermakova.todoapp.data.di.module.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class AddGoalModule {

    @Provides
    @IntoMap
    @ViewModelKey(AddGoalViewModel::class)
    fun provideViewModel(viewModel: AddGoalViewModel): ViewModel = viewModel
}
package by.aermakova.todoapp.ui.task.addNew

import androidx.lifecycle.ViewModel
import by.aermakova.todoapp.data.di.module.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class AddTaskModule {

    @Provides
    @IntoMap
    @ViewModelKey(AddTaskViewModel::class)
    fun provideViewModel(viewModel: AddTaskViewModel): ViewModel = viewModel
}
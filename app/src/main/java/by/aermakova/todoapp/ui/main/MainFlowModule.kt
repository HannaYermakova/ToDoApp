package by.aermakova.todoapp.ui.main

import androidx.lifecycle.ViewModel
import by.aermakova.todoapp.data.di.module.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class MainFlowModule {

    @Provides
    @IntoMap
    @ViewModelKey(MainFlowViewModel::class)
    fun providesViewModel(viewModel: MainFlowViewModel): ViewModel = viewModel
}
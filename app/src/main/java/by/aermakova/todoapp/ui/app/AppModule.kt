package by.aermakova.todoapp.ui.app

import androidx.lifecycle.ViewModel
import by.aermakova.todoapp.data.di.module.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class AppModule {

    @Provides
    @IntoMap
    @ViewModelKey(AppViewModel::class)
    fun provideViewModel(viewModel: AppViewModel): ViewModel = viewModel
}
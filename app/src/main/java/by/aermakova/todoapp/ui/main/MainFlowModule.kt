package by.aermakova.todoapp.ui.main

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class MainFlowModule {

    @Provides
    fun provideMainFlowNavigation(activity: Activity): MainFlowNavigation.Settings {
        val controller = Navigation.findNavController(activity, R.id.main_host_fragment)
        return MainFlowNavigationSetting(controller)
    }

    @Provides
    @IntoMap
    @ViewModelKey(MainFlowViewModel::class)
    fun providesViewModel(viewModel: MainFlowViewModel): ViewModel = viewModel
}
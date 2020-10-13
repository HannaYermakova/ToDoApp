package by.aermakova.todoapp.ui.goal

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
class AddGoalModule {

    @Provides
    fun provideGoalsNavigation(activity: Activity): MainFlowNavigation {
        val controller = Navigation.findNavController(activity, R.id.app_host_fragment)
        return GoalsNavigation(controller)
    }

    @Provides
    @IntoMap
    @ViewModelKey(AddGoalViewModel::class)
    fun provideViewModel(viewModel: AddGoalViewModel): ViewModel = viewModel
}
package by.aermakova.todoapp.ui.goal

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.data.di.module.ViewModelKey
import by.aermakova.todoapp.ui.navigation.FragmentNavigation
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap


@Module
class GoalsModule {

    @Provides
    fun provideGoalsNavigation(activity: Activity): FragmentNavigation {
        val controller = Navigation.findNavController(activity, R.id.app_host_fragment)
        return GoalsNavigation(controller)
    }

    @Provides
    @IntoMap
    @ViewModelKey(GoalsViewModel::class)
    fun provideViewModel(goalsViewModel: GoalsViewModel): ViewModel = goalsViewModel

}
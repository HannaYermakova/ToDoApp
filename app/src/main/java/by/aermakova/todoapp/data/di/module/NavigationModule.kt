package by.aermakova.todoapp.data.di.module

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import dagger.Module
import dagger.Provides

@Module
class NavigationModule {

    @Provides
    fun provideNavigationController(view: View): NavController {
        return Navigation.findNavController(view)
    }
}
package by.aermakova.todoapp.ui.dialog

import android.app.Activity
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import dagger.Module
import dagger.Provides

@Module
class AddItemModule {

    @Provides
    fun provideDialogNavigation(activity: Activity) : DialogNavigation{
        val navController =  Navigation.findNavController(activity, R.id.app_host_fragment)
        return AddItemDialogNavigation(navController)
    }
}
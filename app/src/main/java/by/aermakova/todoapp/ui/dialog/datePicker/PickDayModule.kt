package by.aermakova.todoapp.ui.dialog.datePicker

import android.app.Activity
import android.app.DatePickerDialog
import androidx.navigation.Navigation
import by.aermakova.todoapp.R
import dagger.Module
import dagger.Provides

@Module
class PickDayModule {

    @Provides
    fun provideDialogNavigation(activity: Activity): DatePickerDialog.OnDateSetListener {
        val navController = Navigation.findNavController(activity, R.id.app_host_fragment)
        return PickDayDialogNavigator(navController)
    }
}
package by.aermakova.todoapp.ui.dialog.datePicker

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import by.aermakova.todoapp.AppNavigationDirections
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import java.util.*


class PickDayDialogNavigator(private val controller: NavController) :
    DatePickerDialog.OnDateSetListener,
    DialogNavigation<Long> {

    companion object {
        private const val DIALOG_RESULT = "pick_day_dialog_result"
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        setDialogResult(convertDateToLong(year, month, day))
    }

    private fun convertDateToLong(year: Int, month: Int, day: Int): Long {
        return Calendar.getInstance().apply {
            set(year, month, day)
        }.timeInMillis
    }

    override fun openItemDialog(title: String) {
        controller.navigate(AppNavigationDirections.actionGlobalPickDayDialogFragment())
    }

    override fun getDialogResult(): MutableLiveData<Long>? {
        return controller.currentBackStackEntry?.savedStateHandle?.getLiveData<Long>(DIALOG_RESULT)
    }

    override fun setDialogResult(result: Long) {
        controller.previousBackStackEntry?.savedStateHandle?.set(DIALOG_RESULT, result)
    }
}
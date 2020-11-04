package by.aermakova.todoapp.ui.dialog.datePicker

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import by.aermakova.todoapp.ui.base.BaseDialogFragment
import java.util.*
import javax.inject.Inject

class PickDayDialogFragment : BaseDialogFragment(),
    DatePickerDialog.OnDateSetListener {

    @Inject
    lateinit var listener: DatePickerDialog.OnDateSetListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        return activity?.let {
            DatePickerDialog(it, this, year, month, day)
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        listener.onDateSet(view, year, month, day)
    }
}
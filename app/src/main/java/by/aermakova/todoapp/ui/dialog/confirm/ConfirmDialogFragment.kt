package by.aermakova.todoapp.ui.dialog.confirm

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.ui.base.BaseDialogFragment
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import javax.inject.Inject

class ConfirmDialogFragment : BaseDialogFragment() {

    private val args: ConfirmDialogFragmentArgs by navArgs()

    @Inject
    lateinit var router: DialogNavigation<Boolean>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage(args.title)
                .setPositiveButton(R.string.ok)
                { _, _ ->
                    dismiss()
                    router.setDialogResult(true)
                }
                .setNegativeButton(R.string.cancel)
                { _, _ -> dismiss() }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
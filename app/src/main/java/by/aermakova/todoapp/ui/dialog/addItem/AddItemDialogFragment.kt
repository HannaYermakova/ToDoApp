package by.aermakova.todoapp.ui.dialog.addItem

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentAddItemBinding
import by.aermakova.todoapp.ui.base.BaseDialogFragment
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import by.aermakova.todoapp.util.showSnackMessage
import javax.inject.Inject

class AddItemDialogFragment : BaseDialogFragment() {

    private val args: AddItemDialogFragmentArgs by navArgs()

    @Inject
    lateinit var router: DialogNavigation<String>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding: FragmentAddItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_add_item, null, false)

        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage(args.title)
                .setView(binding.root)
                .setPositiveButton(R.string.save_button)
                { _, _ ->
                    val title = binding.dialogEditText.text.toString()
                    Log.d("AddItemDialogFragment", "${title.isBlank()}")
                    if (title.isNotBlank()) {
                        router.setDialogResult(title.trim())
                    } else {
                        binding.root.showSnackMessage(
                            context?.getString(R.string.error_empty_field) ?: ""
                        )
                    }
                }
                .setNegativeButton(R.string.cancel)
                { _, _ -> dismiss() }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
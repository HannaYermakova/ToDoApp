package by.aermakova.todoapp.ui.dialog.addItem

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.DialogAddItemBinding
import by.aermakova.todoapp.ui.base.BaseDialogFragment
import javax.inject.Inject


class AddItemDialogFragment : BaseDialogFragment() {

    @Inject
    lateinit var viewModel: AddItemDialogViewModel

    lateinit var binding: DialogAddItemBinding

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.setVariable(bindingViewModelId, viewModel)
        setDismissListener()
    }

    private fun setDismissListener() {
        disposable.add(viewModel.dismissCommand.subscribe { if (it) dismiss() })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.dialog_add_item, null, false)
        binding.lifecycleOwner = this
        return activity?.let {
            AlertDialog.Builder(it, R.style.FullHeightDialog)
                .setView(binding.root)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
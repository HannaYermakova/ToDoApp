package by.aermakova.todoapp.ui.dialog.selectItem

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentSelectItemBinding
import by.aermakova.todoapp.ui.base.BaseDialogFragment

abstract class SelectItemDialogFragment : BaseDialogFragment() {

    private val viewModel: SelectItemViewModel
        get() = injectViewModel()

    private lateinit var binding: FragmentSelectItemBinding

    private val title: String
        get() = setTitle()

    abstract fun injectViewModel(): SelectItemViewModel

    abstract fun setTitle(): String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.setVariable(bindingViewModelId, viewModel)
        setDismissListener()
    }

    private fun setDismissListener() {
        disposable.add(viewModel.dismissCommand.subscribe { if (it) dismiss() })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_select_item, null, false)
        binding.lifecycleOwner = this

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setMessage(title)
                .setView(binding.root)
                .setNegativeButton(R.string.cancel)
                { _, _ -> dismiss() }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
package by.aermakova.todoapp.ui.dialog.convertIdea

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.DialogConvertIdeaIntoTaskBinding
import by.aermakova.todoapp.ui.base.BaseDialogFragment
import javax.inject.Inject

class ConvertIdeaIntoTaskDialogFragment : BaseDialogFragment() {

    @Inject
    lateinit var viewModel: ConvertIdeaIntoTaskViewModel

    private lateinit var binding: DialogConvertIdeaIntoTaskBinding

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.setVariable(bindingViewModelId, viewModel)
        setDismissListener()
        setPickDateObserver()
    }

    private fun setDismissListener() {
        disposable.add(viewModel.dismissCommand.subscribe { if (it) dismiss() })
    }

    private fun setPickDateObserver() {
        viewModel.createTaskUseCase.selectedFinishDateObserver?.observe(this, Observer {
            viewModel.createTaskUseCase.checkAndSetFinishTime(it)
        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_convert_idea_into_task,
            null,
            false
        )
        binding.lifecycleOwner = this
        return activity?.let {
            AlertDialog.Builder(it)
                .setView(binding.root)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
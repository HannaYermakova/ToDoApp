package by.aermakova.todoapp.ui.task.edit

import android.os.Bundle
import androidx.lifecycle.Observer
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentEditTaskBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class EditTaskFragment : BaseFragment<EditTaskViewModel, FragmentEditTaskBinding>() {

    override val layout: Int
        get() = R.layout.fragment_edit_task

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.editTaskUseCase.selectedFinishDateObserver?.observe(viewLifecycleOwner, Observer {
            viewModel.editTaskUseCase.checkAndSetFinishTime(it)
        })
    }
}
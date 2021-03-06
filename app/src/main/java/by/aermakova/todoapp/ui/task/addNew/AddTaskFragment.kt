package by.aermakova.todoapp.ui.task.addNew

import android.os.Bundle
import androidx.lifecycle.Observer
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentAddTaskBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class AddTaskFragment : BaseFragment<AddTaskViewModel, FragmentAddTaskBinding>() {

    override val layout: Int
        get() = R.layout.fragment_add_task

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.createTaskUseCase.selectedFinishDateObserver?.observe(viewLifecycleOwner, Observer {
            viewModel.createTaskUseCase.checkAndSetFinishTime(it)
        })
    }
}
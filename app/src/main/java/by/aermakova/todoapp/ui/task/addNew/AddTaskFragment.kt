package by.aermakova.todoapp.ui.task.addNew

import android.os.Bundle
import androidx.lifecycle.Observer
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.DialogAddItemBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class AddTaskFragment : BaseFragment<AddTaskViewModel, DialogAddItemBinding>() {

    override val layout: Int
        get() = R.layout.fragment_add_task

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.taskCreator.selectedFinishDateObserver?.observe(viewLifecycleOwner, Observer {
            viewModel.taskCreator.checkAndSetFinishTime(it)
        })
    }
}
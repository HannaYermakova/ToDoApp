package by.aermakova.todoapp.ui.task.addNew

import android.os.Bundle
import androidx.lifecycle.Observer
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentAddItemBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class AddTaskFragment : BaseFragment<AddTaskViewModel, FragmentAddItemBinding>() {

    override val layout: Int
        get() = R.layout.fragment_add_task

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.selectedGoalObserver?.observe(viewLifecycleOwner, Observer {
            viewModel.addTempGoal(it)
        })

        viewModel.selectedKeyResObserver?.observe(viewLifecycleOwner, Observer {
            viewModel.addTempKeyResult(it)
        })

        viewModel.selectedStepObserver?.observe(viewLifecycleOwner, Observer {
            viewModel.addTempStep(it)
        })
        viewModel.taskCreator.selectedFinishTimeObserver?.observe(viewLifecycleOwner, Observer {
//            viewModel.checkAndSetFinishTime(it)
            viewModel.taskCreator.checkAndSetFinishTime(it)
        })
    }
}
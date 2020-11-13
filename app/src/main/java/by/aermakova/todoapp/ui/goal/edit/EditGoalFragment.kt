package by.aermakova.todoapp.ui.goal.edit

import android.os.Bundle
import androidx.lifecycle.Observer
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentEditGoalBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class EditGoalFragment : BaseFragment<EditGoalViewModel, FragmentEditGoalBinding>() {

    override val layout: Int
        get() = R.layout.fragment_edit_goal

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.addNewKeyResultsToGoalUseCase.keyResultObserver?.observe(
            viewLifecycleOwner,
            Observer {
                viewModel.addNewKeyResultsToGoalUseCase.addTempKeyResult(it)
            })
    }
}
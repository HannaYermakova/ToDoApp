package by.aermakova.todoapp.ui.idea.addNew

import android.os.Bundle
import androidx.lifecycle.Observer
import by.aermakova.todoapp.R
import by.aermakova.todoapp.databinding.FragmentAddIdeaBinding
import by.aermakova.todoapp.ui.base.BaseFragment

class AddIdeaFragment : BaseFragment<AddIdeaViewModel, FragmentAddIdeaBinding>() {

    override val layout: Int
        get() = R.layout.fragment_add_idea

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.selectedGoalObserver?.observe(viewLifecycleOwner, Observer {
            viewModel.addTempGoal(it)
        })

        viewModel.selectedKeyResObserver?.observe(viewLifecycleOwner, Observer {
            viewModel.addTempKeyResult(it)
        })
    }
}
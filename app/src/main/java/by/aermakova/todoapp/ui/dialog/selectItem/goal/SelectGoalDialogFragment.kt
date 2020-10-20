package by.aermakova.todoapp.ui.dialog.selectItem.goal

import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.data.model.Goal
import by.aermakova.todoapp.ui.dialog.selectItem.SelectItemDialogFragment
import by.aermakova.todoapp.ui.dialog.selectItem.SelectItemViewModel
import javax.inject.Inject

class SelectGoalDialogFragment : SelectItemDialogFragment<Goal>() {

    @Inject
    lateinit var injectViewModel: SelectGoalViewModel

    override fun injectViewModel() = injectViewModel

    private val args: SelectGoalDialogFragmentArgs by navArgs()

    override fun setTitle(): String = args.title
}

class SelectGoalViewModel @Inject constructor() : SelectItemViewModel() {



}
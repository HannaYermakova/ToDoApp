package by.aermakova.todoapp.ui.dialog.selectItem.goal

import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.ui.adapter.toTextModel
import by.aermakova.todoapp.ui.dialog.selectItem.SelectItemDialogFragment
import by.aermakova.todoapp.ui.dialog.selectItem.SelectItemViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SelectGoalDialogFragment : SelectItemDialogFragment() {

    @Inject
    lateinit var injectedViewModel: SelectGoalViewModel

    override fun injectViewModel() = injectedViewModel

    private val args: SelectGoalDialogFragmentArgs by navArgs()

    override fun setTitle(): String = args.title
}

class SelectGoalViewModel @Inject constructor(
    goalInteractor: GoalInteractor,
    private val dialogNavigation: SelectGoalDialogNavigation
) : SelectItemViewModel() {

    init {
        disposable.add(
            goalInteractor.getAllUndoneGoals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    it.map { entity ->
                        entity.toTextModel { id ->
                            dialogNavigation.setDialogResult(id)
                            _dismissCommand.onNext(true)
                        }
                    }
                }
                .subscribe(
                    { _itemList.onNext(it) },
                    { it.printStackTrace() }
                )
        )
    }

    override fun doOnCancel() {
        TODO("Not yet implemented")
    }

    override fun doOnOk() {
        TODO("Not yet implemented")
    }
}
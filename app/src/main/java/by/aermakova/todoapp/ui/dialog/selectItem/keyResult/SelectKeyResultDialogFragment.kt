package by.aermakova.todoapp.ui.dialog.selectItem.keyResult

import android.os.Bundle
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.data.interactor.GoalInteractor
import by.aermakova.todoapp.data.model.toTextModel
import by.aermakova.todoapp.ui.dialog.selectItem.SelectItemDialogFragment
import by.aermakova.todoapp.ui.dialog.selectItem.SelectItemViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SelectKeyResultDialogFragment : SelectItemDialogFragment() {

    @Inject
    lateinit var injectedViewModel: SelectKeyResultViewModel

    override fun injectViewModel() = injectedViewModel

    private val args: SelectKeyResultDialogFragmentArgs by navArgs()

    override fun setTitle(): String = args.title

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        injectedViewModel.showKeyResult(args.goalId)
    }
}

class SelectKeyResultViewModel @Inject constructor(
    private val goalInteractor: GoalInteractor,
    private val dialogNavigation: SelectKeyResultDialogNavigation
) : SelectItemViewModel() {

    fun showKeyResult(goalId: Long) {
        disposable.add(
            goalInteractor.getGoalKeyResultsById(goalId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    it.keyResults
                        .filter { entity -> !entity.keyResultStatusDone }
                        .map { entity ->
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
package by.aermakova.todoapp.ui.dialog.selectItem.step

import android.os.Bundle
import androidx.navigation.fragment.navArgs
import by.aermakova.todoapp.data.interactor.StepInteractor
import by.aermakova.todoapp.ui.adapter.toTextModel
import by.aermakova.todoapp.ui.dialog.selectItem.SelectItemDialogFragment
import by.aermakova.todoapp.ui.dialog.selectItem.SelectItemViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SelectStepDialogFragment : SelectItemDialogFragment() {

    @Inject
    lateinit var injectedViewModel: SelectStepViewModel

    override fun injectViewModel() = injectedViewModel

    private val args: SelectStepDialogFragmentArgs by navArgs()

    override fun setTitle(): String = args.title

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        injectedViewModel.showSteps(args.keyResultId)
    }
}

class SelectStepViewModel @Inject constructor(
    private val stepInteractor: StepInteractor,
    private val dialogNavigation: SelectStepDialogNavigation
) : SelectItemViewModel() {

    fun showSteps(keyResultIdId: Long) {
        disposable.add(
            stepInteractor.getStepsByKeyResultId(keyResultIdId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    it.filter { entity -> !entity.stepStatusDone }
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
}
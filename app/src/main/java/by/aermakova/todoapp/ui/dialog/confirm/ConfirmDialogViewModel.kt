package by.aermakova.todoapp.ui.dialog.confirm

import by.aermakova.todoapp.ui.base.BaseDialogVieModel
import by.aermakova.todoapp.ui.navigation.DialogNavigation
import javax.inject.Inject

class ConfirmDialogViewModel @Inject constructor(
    val message : String
) : BaseDialogVieModel() {

    @Inject
    lateinit var router: DialogNavigation<Boolean>


    override fun doOnCancel() {

    }

    override fun doOnOk() {
        router.setDialogResult(true)
    }
}
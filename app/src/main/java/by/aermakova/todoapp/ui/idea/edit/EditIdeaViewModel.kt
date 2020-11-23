package by.aermakova.todoapp.ui.idea.edit

import by.aermakova.todoapp.data.db.entity.IdeaEntity
import by.aermakova.todoapp.data.di.scope.NavigationIdeas
import by.aermakova.todoapp.data.useCase.FindIdeaUseCase
import by.aermakova.todoapp.data.useCase.editText.ChangeItemTextUseCase
import by.aermakova.todoapp.ui.base.BaseViewModel
import by.aermakova.todoapp.ui.idea.IdeasNavigation
import by.aermakova.todoapp.ui.navigation.MainFlowNavigation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class EditIdeaViewModel @Inject constructor(
    @NavigationIdeas private val navigation: IdeasNavigation,
    val changeIdeaTextUseCase: ChangeItemTextUseCase<IdeaEntity>,
    findIdeaUseCase: FindIdeaUseCase,
    ideaId: Long
) : BaseViewModel() {

    override val mainFlowNavigation: MainFlowNavigation?
        get() = navigation

    private val _saveIdeaTextSuccess = BehaviorSubject.create<Boolean>()

    val saveStep = {
        changeIdeaTextUseCase.saveChanges(ideaId, disposable, _saveIdeaTextSuccess, errorAction)
    }

    init {
        disposable.add(
            _saveIdeaTextSuccess
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { if (it) popBack.invoke() },
                    { it.printStackTrace() }
                )
        )
        findIdeaUseCase.useIdeasById(disposable, ideaId, {
            changeIdeaTextUseCase.setExistingItemText(it.text)
        }, errorAction)
    }

}
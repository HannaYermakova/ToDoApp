package by.aermakova.todoapp.data.useCase.editText

import io.reactivex.Observable

interface ChangeItemText<Entity> {

    fun updateItemTextLocal(newText: String, itemId: Long): Boolean

    fun updateItemToRemote(entity: Entity?)

    fun getItemById(itemId: Long): Observable<Entity>
}
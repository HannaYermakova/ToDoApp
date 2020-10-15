package by.aermakova.todoapp.util

import android.view.View

fun provideClickToParent(child: View?) {
    val parent = child?.parent as? View
    if (parent != null) {
        if (parent.callOnClick()) {
            return
        } else {
            val view = (parent.parent as? View)
            if (view != null) {
                provideClickToParent(view)
            } else return
        }
    }
}
package by.aermakova.todoapp.util

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar


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

fun View.showSnackMessage(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun Activity.hideKeyboard() {
    val imm: InputMethodManager =
        this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view: View? = this.currentFocus
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
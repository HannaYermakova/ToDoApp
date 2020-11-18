package by.aermakova.todoapp.util

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

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

fun View.showToastMessage(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
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

fun getAttributeColor(
    context: Context,
    attributeId: Int
): Int {
    val typedValue = TypedValue()
    context.theme.resolveAttribute(attributeId, typedValue, true)
    val colorRes = typedValue.resourceId
    return context.resources.getColor(
        colorRes,
        context.theme
    )
}
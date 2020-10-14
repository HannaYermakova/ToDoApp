package by.aermakova.todoapp.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue


fun Int.dpToPx() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Context.pxToDp(px: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, resources.displayMetrics)
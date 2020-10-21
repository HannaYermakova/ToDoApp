package by.aermakova.todoapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.*

fun Int.dpToPx() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Context.pxToDp(px: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, resources.displayMetrics)

@SuppressLint("SimpleDateFormat")
fun convertLongToDate(finishDate: Long): String {
    val date = Date(finishDate)
    val format = SimpleDateFormat("dd.MM.yyyy")
    return format.format(date)
}
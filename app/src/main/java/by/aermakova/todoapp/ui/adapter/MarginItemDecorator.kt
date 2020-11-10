package by.aermakova.todoapp.ui.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import by.aermakova.todoapp.util.dpToPx

class MarginItemDecorator(
    private val leftMargin: Int = 0,
    private val rightMargin: Int = 0,
    private val divide: Int = 0,
    private val linear: Boolean = true
) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        with(outRect) {
            left = leftMargin.dpToPx()
            right = rightMargin.dpToPx()
            bottom = if (parent.adapter != null && position == parent.adapter!!.itemCount) 0 else divide.dpToPx()
            top = if (linear && position == 0) 0 else divide.dpToPx()
        }
    }
}
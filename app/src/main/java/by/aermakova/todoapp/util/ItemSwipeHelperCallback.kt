package by.aermakova.todoapp.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ItemSwipeHelperCallback(builder: Builder) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    private val icon: Drawable
    private val iconId: Int?
    private val context: Context?
    private val onSwipedAction: ((viewHolder: RecyclerView.ViewHolder, direction: Int) -> Unit)?
    private var backgroundColorId: Int? = 0
    private var backgroundColor: Int = 0
    private var intrinsicWidth: Int = 0
    private var intrinsicHeight: Int = 0

    private val background = ColorDrawable()

    init {
        this.iconId = builder.iconId
        this.context = builder.context
        this.backgroundColorId = builder.backgroundColorId
        this.onSwipedAction = builder.onSwipedAction
        icon = ContextCompat.getDrawable(context!!, iconId!!)!!
        backgroundColor = context.resources?.getColor(
            backgroundColorId!!,
            context.theme
        ) ?: 0
        intrinsicWidth = icon.intrinsicWidth
        intrinsicHeight = icon.intrinsicHeight
    }

    class Builder {
        var iconId: Int? = null
            private set

        var context: Context? = null
            private set

        var backgroundColorId: Int? = null
            private set

        var onSwipedAction: ((viewHolder: RecyclerView.ViewHolder, direction: Int) -> Unit)? = null
            private set

        fun iconId(iconId: Int) = apply { this.iconId = iconId }
        fun context(context: Context) = apply { this.context = context }
        fun backgroundColorId(colorId: Int) = apply { this.backgroundColorId = colorId }
        fun onSwipedAction(action: (viewHolder: RecyclerView.ViewHolder, direction: Int) -> Unit) =
            apply { this.onSwipedAction = action }

        fun build() = ItemSwipeHelperCallback(this)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false


    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(
                canvas,
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(
                canvas,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
            return
        }

        background.color = backgroundColor
        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(canvas)

        val iconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val iconBottom = iconTop + intrinsicHeight
        val iconMargin = (itemHeight - intrinsicHeight) / 2
        val iconLeft = itemView.right - iconMargin - intrinsicWidth
        val iconRight = itemView.right - iconMargin

        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        icon.draw(canvas)

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwipedAction?.invoke(viewHolder, direction)
    }

    private fun clearCanvas(canvas: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        canvas?.drawRect(left, top, right, bottom, clearPaint)
    }
}
package by.aermakova.todoapp.ui.progress

import android.animation.ValueAnimator
import android.graphics.*
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class BallImage(
    private val radius: Float,
    private val centerX: Float,
    private var centerY: Float
) {

    var color: Int = Color.BLUE
        set(value) {
            field = value
            paint.color = value
        }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    var delay: Long = 0
    private val moveDuration = 250L
    private var animator: ValueAnimator? = null

    fun draw(canvas: Canvas) {
        canvas.drawCircle(centerX, centerY, radius, paint)
    }

    fun startAnimation(view: View, from: Float, to: Float) {
        animator = ValueAnimator.ofFloat(from, to).apply {
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            duration = moveDuration
            interpolator = AccelerateDecelerateInterpolator()
            startDelay = delay
            addUpdateListener { valueAnimator ->
                centerY = (valueAnimator.animatedValue as Float)
                view.invalidate()
            }
        }
        animator?.start()
        view.invalidate()
    }

    fun stopAnimation() {
        animator?.cancel()
    }
}
package by.aermakova.todoapp.ui.progress

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import by.aermakova.todoapp.R
import by.aermakova.todoapp.util.pxToDp

class BallsProgressIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs) {

    private var ballColor = R.color.color_scheme_salt_box
    private var ballRadius = context.pxToDp(7f)
    private var distanceBetween = context.pxToDp(20f)
    private var paneHeight = context.pxToDp(100f)
    private var paneWidth = ballRadius * 6 + distanceBetween * 2

    private var balls: Array<BallImage>? = null

    init {
        balls = Array(3) { i -> createBallImage(i) }
    }

    private fun createBallImage(i: Int): BallImage {
        return BallImage(
            ballRadius,
            ((2 * i + 1) * ballRadius + (distanceBetween * i)),
            (paneHeight - ballRadius)
        ).apply {
            color = ballColor
            delay = i * 250L
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawBalls(canvas, balls)
    }

    private fun drawBalls(canvas: Canvas, balls: Array<BallImage>?) {
        balls?.forEach { ball ->
            ball.draw(canvas)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minW = (paddingLeft + paddingRight + paneWidth).toInt()
        val w: Int = resolveSizeAndState(minW, widthMeasureSpec, 0)
        val minH = (paddingTop + paddingBottom + paneHeight).toInt()
        val h = resolveSizeAndState(minH, heightMeasureSpec, 0)
        setMeasuredDimension(w, h)
    }

    fun startAnimation() {
        balls?.forEach { ball ->
            ball.startAnimation(this, (paneHeight - ballRadius), ballRadius)
        }
    }

    fun stopAnimation() {
        balls?.forEach { ball ->
            ball.stopAnimation()
        }
    }
}
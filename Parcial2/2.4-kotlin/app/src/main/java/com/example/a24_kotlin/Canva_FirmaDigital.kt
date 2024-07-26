package com.example.a24_kotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.Nullable

class Canva_FirmaDigital @JvmOverloads constructor(
    context: Context,
    @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        @JvmStatic var isTouched: Boolean = false
    }

    private val paint = Paint()
    private val path = Path()
    private var bitmap: Bitmap? = null
    private var canvas: Canvas? = null

    init {
        init(attrs, defStyleAttr)
    }

    private fun init(@Nullable attrs: AttributeSet?, defStyle: Int) {
        paint.isAntiAlias = true
        paint.strokeWidth = 6f
        paint.color = 0xFF000000.toInt() // Set the default color to black
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val xPos = event.x
        val yPos = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isTouched = true
                path.moveTo(xPos, yPos)
                return true
            }
            MotionEvent.ACTION_MOVE -> path.lineTo(xPos, yPos)
            MotionEvent.ACTION_UP -> {
                canvas?.drawPath(path, paint)
                // path.reset() // Uncomment this if you want to reset the path after drawing
            }
            else -> return false
        }

        invalidate() // Redraw
        return true
    }

    fun clearCanvas() {
        path.reset()
        canvas?.drawColor(0xFFFFFFFF.toInt()) // Clear with white color
        invalidate()
    }

    fun getSignatureBitmap(): Bitmap? {
        return bitmap
    }
}
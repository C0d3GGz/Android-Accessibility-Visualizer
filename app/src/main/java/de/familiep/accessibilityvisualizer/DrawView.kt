package de.familiep.accessibilityvisualizer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View

class DrawView(context: Context) : View(context) {

    private val paint = Paint()
    private val rectList: MutableList<Rect> = mutableListOf()

    init {
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS
        initPaint()
    }

    private fun initPaint(){
        paint.color = Color.CYAN
        paint.strokeWidth = 3f
        paint.style = Paint.Style.STROKE
        paint.textSize = 50f
    }

    fun addRect(rectToDraw: Rect) {
        rectList.add(rectToDraw)
        invalidate()
    }

    fun clear() {
        rectList.clear()
        invalidate()
    }

    public override fun onDraw(canvas: Canvas) {
        rectList.forEach {
            canvas.drawRect(it, paint)
        }
    }
}
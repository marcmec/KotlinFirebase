package com.example.marcoseng.kotlin_firebase.helper

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF

class RectOverlay internal constructor(overlay: GraphicOverlay,
                                        private val bound: Rect?):GraphicOverlay.Graphic(overlay) {

    private val rectPaint: Paint

    init{
    rectPaint = Paint()
    rectPaint.color
    rectPaint.strokeWidth= 4.0f
        rectPaint.style = Paint.Style.STROKE
    }
    override fun draw(canvas: Canvas) {

            val rect = RectF(bound)
                rect.left = translateX(rect.left)
                rect.right= translateX(rect.right)
                rect.top= translateY(rect.top)
                rect.bottom= translateY(rect.bottom)

        canvas.drawRect(rect,rectPaint)
    }
}
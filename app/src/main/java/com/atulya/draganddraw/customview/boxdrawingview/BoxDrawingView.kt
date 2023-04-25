package com.atulya.draganddraw.customview.boxdrawingview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.atulya.draganddraw.customview.boxdrawingview.helperclasses.Box

class BoxDrawingView(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val boxList = mutableListOf<Box>()
    private var currentBox: Box? = null

    private val boxPaint = Paint().apply {
        color = 0x80003cc8.toInt()
    }
    private val backgroundPaint = Paint().apply {
        color = 0x33002891
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        /**
         * [PointF] is a class we can use to keep coordinates
         * togather.
         */
        val current = PointF(event.x, event.y)

        val action = when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentBox = Box(current).also {
                    boxList.add(it)
                }
                "DOWN"
            }

            MotionEvent.ACTION_MOVE -> {
                updateCurrentBox(current)
                "MOVE"
            }

            MotionEvent.ACTION_UP -> {
                updateCurrentBox(current)
                currentBox = null
                Log.d("#> ${this::class.simpleName}", "$boxList")
                "UP"
            }

            MotionEvent.ACTION_CANCEL -> {
                currentBox = null
                "CANCEL"
            }

            else -> ""
        }

        Log.d(
            "#> ${this::class.simpleName}",
            "$action at $current"
        )

        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPaint(backgroundPaint)

        boxList.forEach { box ->
            canvas.drawRect(box.left, box.top, box.right, box.bottom, boxPaint)
        }
    }

    private fun updateCurrentBox(current: PointF) {
        currentBox?.let {
            it.end = current
            invalidate() // Force the view to redraw itself
        }
    }
}
package com.atulya.draganddraw.customview.boxdrawingview

import android.content.Context
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

    override fun onTouchEvent(event: MotionEvent): Boolean {

        /**
         * [PointF] is a class we can use to keep coordinates
         * togather.
         */
        val current = PointF(event.x, event.y)

        val action = when(event.action){
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

    private fun updateCurrentBox(current: PointF) {
        currentBox?.let {
            it.end = current
            invalidate() // Force the view to redraw itself
        }
    }
}
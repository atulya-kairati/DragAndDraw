package com.atulya.draganddraw.customview.boxdrawingview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.atulya.draganddraw.customview.boxdrawingview.helperclasses.Box

class BoxDrawingView(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var boxes = ArrayList<Box>()
    private var currentBox: Box? = null

    private val boxPaint = Paint().apply {
        color = 0x80003cc8.toInt()
    }
    private val backgroundPaint = Paint().apply {
        color = 0x33002891
    }

    init {
        isSaveEnabled = true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        /**
         * [PointF] is a class we can use to keep coordinates
         * together.
         */
        val current = PointF(event.x, event.y)

        val action = when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentBox = Box(current).also {
                    boxes.add(it)
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
                Log.d("#> ${this::class.simpleName}", "$boxes")
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

        boxes.forEach { box ->
            canvas.drawRect(box.left, box.top, box.right, box.bottom, boxPaint)
        }
    }

    private fun updateCurrentBox(current: PointF) {
        currentBox?.let {
            it.end = current
            invalidate() // Force the view to redraw itself
        }
    }

    override fun onSaveInstanceState(): Parcelable {

        val bundle = Bundle()

        bundle.putParcelableArrayList(BOXES_TAG, boxes)
        bundle.putParcelable(PARENT_PARCEL_TAG, super.onSaveInstanceState())

        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {

        val parentParcel: Parcelable? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            (state as Bundle).getParcelable(PARENT_PARCEL_TAG, Parcelable::class.java)
        } else {
            (state as Bundle).getParcelable(PARENT_PARCEL_TAG)
        }

        boxes = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            state.getParcelableArrayList(BOXES_TAG, Box::class.java) as ArrayList<Box>
        } else {
            state.get(BOXES_TAG) as ArrayList<Box>
        }
        Log.d("#> ${this::class.simpleName}", "$boxes")

        super.onRestoreInstanceState(parentParcel)
    }

    companion object {
        const val BOXES_TAG = "boxes"
        const val PARENT_PARCEL_TAG = "parentParcel"
    }
}
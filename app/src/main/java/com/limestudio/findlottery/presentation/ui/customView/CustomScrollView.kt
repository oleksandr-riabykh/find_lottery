package com.limestudio.findlottery.presentation.ui.customView

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.widget.ScrollView


class CustomScrollView(context: Context?, attrs: AttributeSet?) :
    ScrollView(context, attrs) {
    private val mGestureDetector: GestureDetector
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return (super.onInterceptTouchEvent(ev)
                && mGestureDetector.onTouchEvent(ev))
    }

    // Return false if we're scrolling in the x direction
    internal inner class YScrollDetector : SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent, e2: MotionEvent,
            distanceX: Float, distanceY: Float
        ): Boolean {
            return Math.abs(distanceY) > Math.abs(distanceX)
        }
    }

    init {
        mGestureDetector = GestureDetector(context, YScrollDetector())
        setFadingEdgeLength(0)
    }
}
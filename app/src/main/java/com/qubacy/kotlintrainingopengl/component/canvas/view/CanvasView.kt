package com.qubacy.kotlintrainingopengl.component.canvas.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import com.qubacy.kotlintrainingopengl.component.canvas.renderer.CanvasRenderer

class CanvasView(
    context: Context,
    attrs: AttributeSet
) : GLSurfaceView(context, attrs),
    OnScaleGestureListener {
    companion object {
        const val TAG = "CANVAS_VIEW"

        private const val TOUCH_SCALE_FACTOR: Float = 180.0f / 12800f //320f
        private const val AFTER_SCALE_DELAY = 300L
    }

    private val mRenderer: CanvasRenderer
    private val mScaleGestureDetector: ScaleGestureDetector

    private var mLastScaleEventTimestamp = 0L

    init {
        setEGLContextClientVersion(2)

        mRenderer = CanvasRenderer()
        mScaleGestureDetector = ScaleGestureDetector(context, this)

        setRenderer(mRenderer)

        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

    private var previousX: Float = 0f
    private var previousY: Float = 0f

    override fun onTouchEvent(e: MotionEvent): Boolean {
        val x: Float = e.x
        val y: Float = e.y

        if (e.pointerCount == 2) {
            mScaleGestureDetector.onTouchEvent(e)
            requestRender()

            return true

        } else if (e.pointerCount > 2)
            return false

        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                val curTime = System.currentTimeMillis()

                Log.d(TAG, "onTouchEvent(): ACTION_MOVE: mLastScaleEventTimestamp = $mLastScaleEventTimestamp; curTime = $curTime")

                if (mLastScaleEventTimestamp + AFTER_SCALE_DELAY > curTime)
                    return false

                val dx: Float = x - previousX
                val dy: Float = y - previousY

                mRenderer.handleRotation(dx * TOUCH_SCALE_FACTOR, dy * TOUCH_SCALE_FACTOR)

                requestRender()
            }
        }

        previousX = x
        previousY = y

        return true
    }

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        mRenderer.handleScale(detector.scaleFactor)

        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector) {
        mLastScaleEventTimestamp = System.currentTimeMillis()
    }
}
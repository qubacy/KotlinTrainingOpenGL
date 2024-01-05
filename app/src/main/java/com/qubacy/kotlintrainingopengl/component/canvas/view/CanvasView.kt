package com.qubacy.kotlintrainingopengl.component.canvas.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent
import com.qubacy.kotlintrainingopengl.component.canvas.renderer.CanvasRenderer

class CanvasView(
    context: Context,
    attrs: AttributeSet
) : GLSurfaceView(context, attrs) {
    companion object {
        private const val TOUCH_SCALE_FACTOR: Float = 180.0f / 12800f //320f
    }

    private val mRenderer: CanvasRenderer

    init {
        setEGLContextClientVersion(2)

        mRenderer = CanvasRenderer()

        setRenderer(mRenderer)

        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

    private var previousX: Float = 0f
    private var previousY: Float = 0f

    override fun onTouchEvent(e: MotionEvent): Boolean {
        val x: Float = e.x
        val y: Float = e.y

        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                var dx: Float = x - previousX
                var dy: Float = y - previousY

                mRenderer.handleRotation(dx * TOUCH_SCALE_FACTOR, dy * TOUCH_SCALE_FACTOR)

                requestRender()
            }
        }

        previousX = x
        previousY = y

        return true
    }
}
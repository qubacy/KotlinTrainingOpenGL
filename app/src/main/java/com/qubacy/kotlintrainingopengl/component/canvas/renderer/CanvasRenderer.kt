package com.qubacy.kotlintrainingopengl.component.canvas.renderer

import android.opengl.GLES20
import android.opengl.GLSurfaceView.Renderer
import android.opengl.Matrix
import com.qubacy.kotlintrainingopengl.component.canvas.renderer.geometry._common.Figure
import com.qubacy.kotlintrainingopengl.component.canvas.renderer.geometry.square.Square
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CanvasRenderer : Renderer {
    private val mMVPMatrix = FloatArray(16)
    private val mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)

    private lateinit var mFigure: Figure

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        mFigure = Square(
            floatArrayOf(
                -0.5f,  0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f,  0.5f, 0.0f
            )
        )
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio: Float = width.toFloat() / height.toFloat()

        Matrix.frustumM(
            mProjectionMatrix, 0,
            -ratio, ratio,
            -1f, 1f,
            3f, 7f
        )
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        Matrix.setLookAtM(
            mViewMatrix, 0,
            0f, 0f, 3f,
            0f, 0f, 0f,
            0f, 1.0f, 0.0f
        )
        Matrix.multiplyMM(
            mMVPMatrix, 0,
            mProjectionMatrix, 0,
            mViewMatrix, 0
        )

        mFigure.draw(mMVPMatrix)
    }
}
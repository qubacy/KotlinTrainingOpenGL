package com.qubacy.kotlintrainingopengl.component.canvas.renderer

import android.opengl.GLES20
import android.opengl.GLSurfaceView.Renderer
import android.opengl.Matrix
import com.qubacy.kotlintrainingopengl.component.canvas.renderer.geometry._common.Figure
import com.qubacy.kotlintrainingopengl.component.canvas.renderer.geometry.parallelepiped.Parallelepiped
import com.qubacy.kotlintrainingopengl.component.canvas.renderer.geometry.square.Square
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.abs

class CanvasRenderer : Renderer {
    companion object {

    }

    private val mVPMatrix = FloatArray(16)
    private val mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)

    private lateinit var mFigure: Figure

    @Volatile
    private var angle = 0f

    private var mRotationAxis = floatArrayOf(0f, 0f, -1f)

    fun handleRotation(dx: Float, dy: Float) {
        mRotationAxis =
            if (abs(dx) > abs(dy)) {
                angle += dx

                floatArrayOf(0f, 0f, -1f)
            }
            else {
                angle += dy

                floatArrayOf(0f, -1f, 0f)
            }
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        mFigure = Parallelepiped(
            floatArrayOf(
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, 0.5f
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
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        Matrix.setLookAtM(
            mViewMatrix, 0,
            3.505f, 0f, 3.505f,
            0f, 0f, 0f,
            0f, 0f, 1.0f
        )
        Matrix.multiplyMM(
            mVPMatrix, 0,
            mProjectionMatrix, 0,
            mViewMatrix, 0
        )

        val rotationMatrix = FloatArray(16)

        Matrix.setRotateM(
            rotationMatrix, 0,
            angle,
            mRotationAxis[0], mRotationAxis[1], mRotationAxis[2]
        )

        val rotated = FloatArray(16)

        Matrix.multiplyMM(rotated, 0, mVPMatrix, 0, rotationMatrix, 0)

        mFigure.draw(rotated)
    }
}
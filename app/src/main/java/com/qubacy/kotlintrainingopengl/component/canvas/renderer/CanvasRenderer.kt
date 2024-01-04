package com.qubacy.kotlintrainingopengl.component.canvas.renderer

import android.opengl.GLES20
import android.opengl.GLSurfaceView.Renderer
import android.opengl.Matrix
import android.util.Log
import com.qubacy.kotlintrainingopengl.component.canvas.renderer.geometry._common.Figure
import com.qubacy.kotlintrainingopengl.component.canvas.renderer.geometry.parallelepiped.Parallelepiped
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class CanvasRenderer : Renderer {
    companion object {
        const val TAG = "CANVAS_RENDERER"

        private val CENTER_POSITION = floatArrayOf(0f, 0f, 0f)
    }

    private val mVPMatrix = FloatArray(16)
    private val mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)

    private lateinit var mFigure: Figure

    private var mCameraRadius = 3.5f

    @Volatile
    private var mCameraCenterLocation = floatArrayOf(0f, 0f, 3.5f)
    @Volatile
    private var mCameraLocation = floatArrayOf(mCameraRadius, 0f, mCameraCenterLocation[2])//(3.505f, 0f, 3.505f)
    @Volatile
    private var mCameraMadeWay = 0f

    private fun getTranslatedCameraLocation(dx: Float, dy: Float): FloatArray {
        val signedDX = dx * -1
        val signedDY = dy * -1

        if (abs(signedDX) < abs(signedDY)) return mCameraLocation

        val cameraWayLength = (2 * PI * mCameraRadius).toFloat()
        val cameraMadeWay = (signedDX + mCameraMadeWay) % cameraWayLength
        val cameraMadeWayNormalized =
            if (cameraMadeWay < 0) cameraMadeWay + cameraWayLength
            else cameraMadeWay

        val madeWayAngle = ((180 * cameraMadeWayNormalized) / (PI * mCameraRadius)).toFloat()

        val newX = mCameraCenterLocation[0] + mCameraRadius * cos(madeWayAngle)
        val newY = mCameraCenterLocation[1] + mCameraRadius * sin(madeWayAngle)

        mCameraMadeWay = cameraMadeWayNormalized

        Log.d(TAG, "getTranslatedCameraLocation(): dx = $dx; newX = $newX; newY = $newY")

        return floatArrayOf(newX, newY, mCameraLocation[2])
    }

    fun handleRotation(dx: Float, dy: Float) {
        mCameraLocation = getTranslatedCameraLocation(dx, dy)
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
            mCameraLocation[0], mCameraLocation[1], mCameraLocation[2],
            0f, 0f, 0f,
            0f, 0f, 1.0f
        )
        Matrix.multiplyMM(
            mVPMatrix, 0,
            mProjectionMatrix, 0,
            mViewMatrix, 0
        )

//        val rotationMatrix = FloatArray(16)

//        Matrix.setRotateM(
//            rotationMatrix, 0,
//            angle,
//            mRotationAxis[0], mRotationAxis[1], mRotationAxis[2]
//        )

//        val rotated = FloatArray(16)
//
//        Matrix.multiplyMM(rotated, 0, mVPMatrix, 0, rotationMatrix, 0)

        mFigure.draw(mVPMatrix)
    }
}
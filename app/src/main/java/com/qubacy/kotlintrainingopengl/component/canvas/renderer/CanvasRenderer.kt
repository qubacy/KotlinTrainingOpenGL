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
import kotlin.math.sqrt

class CanvasRenderer : Renderer {
    companion object {
        const val TAG = "CANVAS_RENDERER"

        private val CENTER_POSITION = floatArrayOf(0f, 0f, 0f)
        private const val DEFAULT_SPHERE_RADIUS = 7f

        private const val MIN_SCALE_FACTOR = 0.25f
        private const val MAX_SCALE_FACTOR = 3f
    }

    private val mVPMatrix = FloatArray(16)
    private val mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)

    private lateinit var mFigure: Figure

    private var mSphereRadius = DEFAULT_SPHERE_RADIUS
    private var mCameraRadius = mSphereRadius

    @Volatile
    private var mCameraCenterLocation = floatArrayOf(0f, 0f, 0f)
    @Volatile
    private var mCameraLocation = floatArrayOf(mCameraRadius, 0f, mCameraCenterLocation[2])
    @Volatile
    private var mCameraMadeWayHorizontal = 0f
    @Volatile
    private var mCameraMadeWayVertical = 0f
    @Volatile
    private var mViewportRatio = 1f
    @Volatile
    private var mCurScaleFactor = 1f

    private fun getTranslatedCameraLocation(dx: Float, dy: Float): FloatArray {
        val signedDX = dx * -1
        val signedDY = dy * 1

        var newX = mCameraLocation[0]
        var newY = mCameraLocation[1]
        var newZ = mCameraLocation[2]

        if (abs(signedDX) >= abs(signedDY)) {
            val cameraWayLength = (2 * PI * mCameraRadius).toFloat()
            val cameraMadeWay = (signedDX + mCameraMadeWayHorizontal) % cameraWayLength
            val cameraMadeWayNormalized =
                if (cameraMadeWay < 0) cameraMadeWay + cameraWayLength
                else cameraMadeWay

            val madeWayAngle = cameraMadeWayNormalized / mCameraRadius

            newX = mCameraCenterLocation[0] + mCameraRadius * cos(madeWayAngle)
            newY = mCameraCenterLocation[1] + mCameraRadius * sin(madeWayAngle)

            mCameraMadeWayHorizontal = cameraMadeWayNormalized

        } else {
            val cameraWayLength = (PI * mSphereRadius / 2).toFloat()
            val cameraMadeWayNormalized = signedDY + mCameraMadeWayVertical

            Log.d(TAG, "getTranslatedCameraLocation(): cameraMadeWayNormalized = $cameraMadeWayNormalized;")

            if (abs(cameraMadeWayNormalized) >= cameraWayLength) return mCameraLocation

            val madeWayAngleVertical = cameraMadeWayNormalized / mSphereRadius

            Log.d(TAG, "getTranslatedCameraLocation(): madeWayAngleVertical = $madeWayAngleVertical;")

            newZ = CENTER_POSITION[2] + mSphereRadius * sin(madeWayAngleVertical)
            val newCameraRadius = sqrt(mSphereRadius * mSphereRadius - newZ * newZ)

            mCameraMadeWayHorizontal *= (newCameraRadius / mCameraRadius)
            mCameraRadius = newCameraRadius

            val madeWayAngleHorizontal = mCameraMadeWayHorizontal / mCameraRadius

            newX = mCameraCenterLocation[0] + mCameraRadius * cos(madeWayAngleHorizontal)
            newY = mCameraCenterLocation[1] + mCameraRadius * sin(madeWayAngleHorizontal)

            mCameraMadeWayVertical = cameraMadeWayNormalized
        }

        Log.d(TAG, "getTranslatedCameraLocation(): dx = $dx; dy = $dy; newX = $newX; newY = $newY; newZ = $newZ")

        return floatArrayOf(newX, newY, newZ)
    }

    fun handleRotation(dx: Float, dy: Float) {
        mCameraLocation = getTranslatedCameraLocation(dx, dy)
    }

    fun handleScale(scaleFactor: Float) {
        val newScaleFactor = mCurScaleFactor * (1 / scaleFactor)

        if (newScaleFactor !in MIN_SCALE_FACTOR..MAX_SCALE_FACTOR) return

        mCurScaleFactor = newScaleFactor

        Matrix.frustumM(
            mProjectionMatrix, 0,
            -mViewportRatio, mViewportRatio,
            -1f, 1f,
            3f, mSphereRadius * mCurScaleFactor + 1f
        )
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
        mCameraLocation = getTranslatedCameraLocation(0f, 12f)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        mViewportRatio = width.toFloat() / height.toFloat()

        Matrix.frustumM(
            mProjectionMatrix, 0,
            -mViewportRatio, mViewportRatio,
            -1f, 1f,
            3f, mSphereRadius + 1f
        )
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        Matrix.setLookAtM(
            mViewMatrix, 0,
            mCameraLocation[0] * mCurScaleFactor,
            mCameraLocation[1] * mCurScaleFactor,
            mCameraLocation[2] * mCurScaleFactor,
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
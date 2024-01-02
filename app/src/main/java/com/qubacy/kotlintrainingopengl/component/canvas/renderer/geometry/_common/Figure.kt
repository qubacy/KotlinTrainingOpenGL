package com.qubacy.kotlintrainingopengl.component.canvas.renderer.geometry._common

import android.opengl.GLES20
import com.qubacy.kotlintrainingopengl.component.canvas.renderer._common.util.GL2Util
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

abstract class Figure(
    val vertexArray: FloatArray,
    val vertexDrawingOrder: ShortArray
) {
    companion object {
        const val COORDS_PER_VERTEX = 3
    }

    protected open val mVertexShaderCode =
        "uniform mat4 uVPMatrix;" +
        "attribute vec4 vPosition;" +
        "void main() {" +
        "  gl_Position = uVPMatrix * vPosition;" +
        "}"
    protected open val mFragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}"
    protected var mProgram: Int

    protected var mVPMatrixHandle: Int = 0

    protected val mVertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(vertexArray.size * Float.SIZE_BYTES).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(vertexArray)
                position(0)
            }
        }
    private val mVertexDrawingOrderBuffer: ShortBuffer =
        ByteBuffer.allocateDirect(vertexDrawingOrder.size * Short.SIZE_BYTES).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(vertexDrawingOrder)
                position(0)
            }
        }
    val vertexCount = vertexArray.size / COORDS_PER_VERTEX

    protected open val mColor: FloatArray = floatArrayOf(1f, 1f, 1f, 1f)

    init {
        val vertexShader = GL2Util.loadShader(GLES20.GL_VERTEX_SHADER, mVertexShaderCode)
        val fragmentShader = GL2Util.loadShader(GLES20.GL_FRAGMENT_SHADER, mFragmentShaderCode)

        mProgram = GLES20.glCreateProgram().apply {
            GLES20.glAttachShader(this, vertexShader)
            GLES20.glAttachShader(this, fragmentShader)

            GLES20.glLinkProgram(this)
        }
    }

    fun draw(mvpMatrix: FloatArray) {
        GLES20.glUseProgram(mProgram)

        mVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uVPMatrix")

        GLES20.glUniformMatrix4fv(mVPMatrixHandle, 1, false, mvpMatrix, 0)

        GLES20.glGetAttribLocation(mProgram, "vPosition").also {
            GLES20.glEnableVertexAttribArray(it)
            GLES20.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                COORDS_PER_VERTEX * Float.SIZE_BYTES,
                mVertexBuffer
            )
            GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->
                GLES20.glUniform4fv(colorHandle, 1, mColor, 0)
            }

            GLES20.glDrawElements(GLES20.GL_TRIANGLES, vertexDrawingOrder.size,
                GLES20.GL_UNSIGNED_SHORT, mVertexDrawingOrderBuffer)
            GLES20.glDisableVertexAttribArray(it)
        }
    }
}
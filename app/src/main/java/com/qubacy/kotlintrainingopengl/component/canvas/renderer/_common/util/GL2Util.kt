package com.qubacy.kotlintrainingopengl.component.canvas.renderer._common.util

import android.opengl.GLES20

object GL2Util {
    fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }
}
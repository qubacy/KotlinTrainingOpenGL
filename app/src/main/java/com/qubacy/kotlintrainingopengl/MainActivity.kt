package com.qubacy.kotlintrainingopengl

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.qubacy.kotlintrainingopengl.component.canvas.renderer.CanvasRenderer

class MainActivity : AppCompatActivity() {
    private lateinit var mCanvas: GLSurfaceView
    private lateinit var mRenderer: CanvasRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mRenderer = CanvasRenderer()
        mCanvas = findViewById<GLSurfaceView?>(R.id.canvas).apply {
            setEGLContextClientVersion(2)
            setRenderer(mRenderer)
        }
    }
}
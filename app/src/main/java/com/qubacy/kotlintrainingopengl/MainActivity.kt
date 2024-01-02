package com.qubacy.kotlintrainingopengl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.qubacy.kotlintrainingopengl.component.canvas.view.CanvasView

class MainActivity : AppCompatActivity() {
    private lateinit var mCanvas: CanvasView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mCanvas = findViewById(R.id.canvas)
    }
}
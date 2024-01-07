package com.qubacy.kotlintrainingopengl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.qubacy.kotlintrainingopengl.geometry.parallelepiped.Parallelepiped
import com.qubacy.kotlintrainingopengl.component.canvas.view.CanvasView
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var mCanvas: CanvasView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mCanvas = findViewById<CanvasView?>(R.id.canvas).apply {
            runBlocking {
                setFigure(
                    Parallelepiped(
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
                )
            }
        }

        val changeButton = findViewById<MaterialButton>(R.id.change_button).apply {
            setOnClickListener { onChangeClicked() }
        }
    }

    private fun onChangeClicked() = runBlocking {
        mCanvas.setFigure(
            Parallelepiped(
                floatArrayOf(
                    -1f, -1f, -1f,
                    -0.5f, -0.5f, 0.5f,
                    -0.5f, 0.5f, -0.5f,
                    -0.5f, 0.5f, 0.5f,
                    0.5f, -0.5f, -0.5f,
                    0.5f, -0.5f, 0.5f,
                    0.5f, 0.5f, -0.5f,
                    0.5f, 0.5f, 0.5f
                )
            )
        )
    }
}
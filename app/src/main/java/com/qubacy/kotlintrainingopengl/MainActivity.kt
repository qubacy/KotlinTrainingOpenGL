package com.qubacy.kotlintrainingopengl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.qubacy.kotlintrainingopengl.geometry.parallelepiped.Parallelepiped
import com.qubacy.kotlintrainingopengl.component.canvas.view.CanvasView
import com.qubacy.kotlintrainingopengl.geometry.custom.CustomFigure
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var mCanvas: CanvasView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mCanvas = findViewById<CanvasView?>(R.id.canvas).apply {
            runBlocking {
                setFigure(
                    CustomFigure(
                        floatArrayOf(
                            0.477108f, 7.289458f, -0.404205f,
                            0.477108f, -1.927824f, -0.404205f,
                            -1.165540f, 7.289458f, -0.404205f,
                            2.119756f, 7.289458f, -0.404205f,
                            2.119756f, -1.927824f, -0.404205f,
                            -1.165540f, -1.927824f, -0.404205f,
                            0.477108f, 7.289458f, 1.406834f,
                            0.477108f, -1.927824f, 1.406834f,
                            0.477108f, 7.235695f, 2.204210f,
                            0.477108f, -1.874061f, 2.204210f,
                            -0.881579f, -1.927824f, 1.477330f,
                            -1.187150f, -1.927824f, 0.673836f,
                            -1.187150f, 7.289458f, 0.673836f,
                            -0.881579f, 7.289458f, 1.477330f,
                            1.835795f, 7.289458f, 1.477330f,
                            2.141366f, 7.289458f, 0.673836f,
                            2.141366f, -1.927824f, 0.673836f,
                            1.835795f, -1.927824f, 1.477330f
                        ),
                        shortArrayOf(
                            10, 7, 9,
                            0, 1, 5,
                            5, 0, 2,
                            11, 5, 7,
                            7, 11, 10,
                            8, 6, 13,
                            10, 13, 12,
                            12, 10, 11,
                            14, 17, 16,
                            16, 14, 15,
                            9, 8, 13,
                            13, 9, 10,
                            15, 3, 6,
                            6, 15, 14,
                            3, 15, 16,
                            16, 3, 4,
                            5, 11, 12,
                            12, 5, 2,
                            13, 6, 2,
                            2, 13, 12,
                            4, 16, 17,
                            17, 4, 7,
                            9, 7, 17,
                            8, 14, 6,
                            1, 0, 3,
                            3, 1, 4,
                            8, 9, 17,
                            17, 8, 14,
                            7, 5, 1,
                            1, 7, 4,
                            0, 2, 6,
                            6, 0, 3
                        )
//                        floatArrayOf(
//                            -0.5f, -0.5f, -0.5f,
//                            -0.5f, -0.5f, 0.5f,
//                            -0.5f, 0.5f, -0.5f,
//                            -0.5f, 0.5f, 0.5f,
//                            0.5f, -0.5f, -0.5f,
//                            0.5f, -0.5f, 0.5f,
//                            0.5f, 0.5f, -0.5f,
//                            0.5f, 0.5f, 0.5f
//                        )
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
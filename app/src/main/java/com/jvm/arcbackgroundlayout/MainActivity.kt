package com.jvm.arcbackgroundlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<ArcBackgroundLayout>(R.id.arcLyt).setDrawArcPointPortrait(0.5f)
        findViewById<ArcBackgroundLayout>(R.id.arcLyt).setCurveControlPointYPortrait(2.55f)
    }
}
package com.bytedance.jstu.homework

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bytedance.jstu.homework.R
import org.w3c.dom.Text

class IntroductionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)

        val tv = findViewById<TextView>(R.id.introduction)
        val bundle = this.intent.extras
        tv.text = bundle?.get("intro").toString()
    }
}
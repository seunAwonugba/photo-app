package com.photoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var instanceOfAcceptButton : Button
    private lateinit var instanceOfRejectButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        instanceOfAcceptButton = findViewById(R.id.acceptId)
        instanceOfRejectButton = findViewById(R.id.rejectId)

        instanceOfAcceptButton.setOnClickListener {
            acceptFunction()
        }

    }

    private fun acceptFunction() {
        val intent = Intent(this, AcceptActivity::class.java)
        startActivity(intent)
    }
}
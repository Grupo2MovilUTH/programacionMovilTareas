package com.example.a22_kotlin

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PostActivity : AppCompatActivity() {

    private lateinit var postTitle: TextView
    private lateinit var postBody: TextView
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        postTitle = findViewById(R.id.postTitle)
        postBody = findViewById(R.id.postBody)
        backButton = findViewById(R.id.backButton)

        val title = intent.getStringExtra("postTitle")
        val body = intent.getStringExtra("postBody")

        postTitle.text = title
        postBody.text = body

        backButton.setOnClickListener { finish() } // Termina la actividad actual y regresa a la anterior
    }
}

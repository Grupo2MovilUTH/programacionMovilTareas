package com.example.a22_kotlin

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var newPostTitleInput: EditText
    private lateinit var newPostBodyInput: EditText
    private lateinit var saveButton: Button
    private lateinit var postsList: ArrayList<Post>
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var postTitles: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        newPostTitleInput = findViewById(R.id.newPostTitleInput)
        newPostBodyInput = findViewById(R.id.newPostBodyInput)
        saveButton = findViewById(R.id.saveButton)
        postsList = ArrayList()
        postTitles = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, postTitles)
        listView.adapter = adapter

        // Ejecutar AsyncTask para obtener datos
        GetPostsTask().execute("https://jsonplaceholder.typicode.com/posts")

        // Configurar el botón Salvar
        saveButton.setOnClickListener {
            val newPostTitle = newPostTitleInput.text.toString()
            val newPostBody = newPostBodyInput.text.toString()
            if (newPostTitle.isNotEmpty() && newPostBody.isNotEmpty()) {
                val newPost = Post(newPostTitle, newPostBody)
                postsList.add(newPost)
                postTitles.add(newPostTitle)
                adapter.notifyDataSetChanged()
                newPostTitleInput.setText("") // Limpiar el campo de título
                newPostBodyInput.setText("")  // Limpiar el campo de cuerpo
                // Mostrar mensaje de confirmación
                Toast.makeText(this@MainActivity, "El post se ha guardado correctamente.", Toast.LENGTH_SHORT).show()
            }
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this@MainActivity, PostActivity::class.java)
            intent.putExtra("postTitle", postsList[position].title)
            intent.putExtra("postBody", postsList[position].body)
            startActivity(intent)
        }
    }

    private inner class GetPostsTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg urls: String): String {
            val result = StringBuilder()
            try {
                val url = URL(urls[0])
                val urlConnection = url.openConnection() as HttpURLConnection
                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    result.append(line)
                }
                reader.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result.toString()
        }

        override fun onPostExecute(result: String) {
            try {
                val jsonArray = JSONArray(result)
                for (i in 0 until jsonArray.length()) {
                    val post = jsonArray.getJSONObject(i)
                    val title = post.getString("title")
                    val body = post.getString("body")
                    postsList.add(Post(title, body))
                    postTitles.add(title)
                }
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

package com.example.randompet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import java.util.*

class MainActivity : AppCompatActivity() {
    var petImageURL = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.petButton)
        val imageView = findViewById<ImageView>(R.id.petImage)
        val animalTypeTextView = findViewById<TextView>(R.id.petType)

        getNextImage(button, imageView, animalTypeTextView)
    }

    private fun getNextImage(button: Button, imageView: ImageView, animalTypeTextView: TextView) {
        button.setOnClickListener {
            val choice = Random().nextInt(2)

            if (choice == 0) {
                getDogImageURL(animalTypeTextView)
            } else {
                getCatImageURL(animalTypeTextView)
            }

            Glide.with(this)
                .load(petImageURL)
                .fitCenter()
                .into(imageView)
        }
    }

    private fun getDogImageURL(animalTypeTextView: TextView) {
        val client = AsyncHttpClient()
        client["https://dog.ceo/api/breeds/image/random/20", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Dog", "response successful$json")
                petImageURL = json.jsonObject.getString("message")
                animalTypeTextView.text = "Dog"
                Log.d("petImageURL", "pet image URL set")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Dog Error", errorResponse)
            }
        }]
    }

    private fun getCatImageURL(animalTypeTextView: TextView) {
        val client = AsyncHttpClient()
        client["https://api.thecatapi.com/v1/images/search", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Cat", "response successful$json")
                val resultsJSON = json.jsonArray.getJSONObject(0)
                petImageURL = resultsJSON.getString("url")
                animalTypeTextView.text = "Cat"
                Log.d("petImageURL", "pet image URL set")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Cat Error", errorResponse)
            }
        }]
    }
}

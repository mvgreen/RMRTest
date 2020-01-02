package com.mvgreen.rmrtest

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.mvgreen.rmrtest.network.json_objects.SearchPhotoResult
import com.mvgreen.rmrtest.network.UnsplashApi
import com.mvgreen.rmrtest.network.json_objects.SearchCollectionResult
import com.mvgreen.rmrtest.network.json_objects.UnsplashPhoto
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.*
import retrofit2.Response
import kotlin.concurrent.thread

const val UNSPLASH_TAG = "UNSPLASH"

class ScrollingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val api = (application as UnsplashApplication).unsplashApi
        var result: Response<List<UnsplashPhoto>>? = null
        thread {
            result = api.showCollection(9042810, 1, 10).execute()
        }.join()

        val res = result
        if (res == null)
            Log.d(UNSPLASH_TAG, "RESULT IS NULL")
        else {
            if (res.isSuccessful)
                largeTextView.text = res.body().toString()
            else
                Log.d(UNSPLASH_TAG, "BAD RESULT: ${res.code()}")
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}

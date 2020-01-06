package com.mvgreen.rmrtest.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mvgreen.rmrtest.EXTRA_PHOTO_INFO
import com.mvgreen.rmrtest.R
import com.mvgreen.rmrtest.model.Repository
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.live_list_item.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    override fun onResume() {
        super.onResume()
        val photoLiveData = MutableLiveData<List<UnsplashPhoto>?>()

        photoLiveData.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    "Could not load photo of the day, check your internet connection",
                    Toast.LENGTH_SHORT
                ).show()
                button_reload.visibility = View.VISIBLE
                photo_of_the_day.visibility = View.GONE

                button_reload.setOnClickListener {
                    Repository.getPhotoOfTheDay(photoLiveData)
                }
                return@Observer
            }
            val photo = it[0]

            val width = resources.displayMetrics.widthPixels
            val height = resources.displayMetrics.heightPixels

            val url = photo.urls.raw + "&" + if (width > height) "w=$width" else "h=$height"

            button_reload.visibility = View.GONE
            photo_of_the_day.visibility = View.VISIBLE

            Picasso.get().load(url)
                .placeholder(resources.getDrawable(R.drawable.baseline_image_24, null))
                .centerCrop()
                .fit()
                .into(photo_of_the_day)

            photo_of_the_day.setOnClickListener {
                startActivity(Intent(this, FullScreenPhotoActivity::class.java).apply {
                    putExtra(EXTRA_PHOTO_INFO, photo)
                })
            }
        })
        Repository.getPhotoOfTheDay(photoLiveData)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.to_search)
            startActivity(Intent(this, SearchActivity::class.java))
        return true
    }
}

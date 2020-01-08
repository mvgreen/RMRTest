package com.mvgreen.rmrtest.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mvgreen.rmrtest.EXTRA_PHOTO_INFO
import com.mvgreen.rmrtest.R
import com.mvgreen.rmrtest.viewmodel.MainViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // This button is shown when the application failed to load 'Photo of the Day' so that the user can try again
        button_reload.setOnClickListener {
            viewModel.getPhotoOfTheDay()
        }

        // Initialize LiveData for 'Photo of the Day' ImageView
        viewModel.observeImage(this, Observer {
            if (it == null) {
                Toast.makeText(
                    this,
                    "Could not load photo of the day, check your internet connection",
                    Toast.LENGTH_SHORT
                ).show()
                // Hide image view and show retry button
                button_reload.visibility = View.VISIBLE
                photo_of_the_day.visibility = View.GONE

                return@Observer
            }
            val photo = it

            val width = resources.displayMetrics.widthPixels
            val height = resources.displayMetrics.heightPixels

            // The downloaded photo will have either a given width or a given height, whichever is less
            val url = photo.urls.raw + "&" + if (width > height) "w=$width" else "h=$height"

            // Hide retry button and show image view
            button_reload.visibility = View.GONE
            photo_of_the_day.visibility = View.VISIBLE

            // Load photo with given size parameters, crop it to fit screen size
            Picasso.get().load(url)
                .placeholder(resources.getDrawable(R.drawable.baseline_image_24, null))
                .centerCrop()
                .fit()
                .into(photo_of_the_day)

            // Open full image in another activity
            photo_of_the_day.setOnClickListener {
                startActivity(Intent(this, FullScreenPhotoActivity::class.java).apply {
                    putExtra(EXTRA_PHOTO_INFO, photo)
                })
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // Request 'Photo of the Day' image
        viewModel.getPhotoOfTheDay()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        // Search icon in appbar just redirects to the actual search activity
        if (id == R.id.to_search)
            startActivity(Intent(this, SearchActivity::class.java))
        return true
    }
}

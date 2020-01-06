package com.mvgreen.rmrtest.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mvgreen.rmrtest.EXTRA_PHOTO_INFO
import com.mvgreen.rmrtest.R
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_fullscreen.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullScreenPhotoActivity : AppCompatActivity() {
    private var mVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)
        mVisible = true

        // Set up the user interaction to manually show or hide the system UI.
        fullscreen_content.setOnClickListener { toggle() }

        val maxHeight = resources.displayMetrics.heightPixels
        val maxWidth = resources.displayMetrics.widthPixels
        val photo =
            intent.getParcelableExtra<UnsplashPhoto>(EXTRA_PHOTO_INFO) ?: throw IllegalStateException("Photo not found")
        val url = photo.urls.raw + "&w=$maxWidth&h=$maxHeight"
        Picasso.get()
            .load(url)
            .placeholder(resources.getDrawable(R.drawable.baseline_image_24, null))
            .into(fullscreen_content)
        photo_info.text = resources.getString(
            R.string.photo_info_template,
            photo.width,
            photo.height,
            photo.description ?: "<NONE>",
            photo.urls.raw
        )
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        fullscreen_content_controls.visibility = View.GONE
        mVisible = false
    }

    private fun show() {
        // Show the system bar
        fullscreen_content_controls.visibility = View.VISIBLE
        mVisible = true
    }

}

package com.mvgreen.rmrtest.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mvgreen.rmrtest.R
import kotlinx.android.synthetic.main.activity_collection_content.*

class CollectionContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_content)
        setSupportActionBar(toolbar)
    }
}

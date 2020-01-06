package com.mvgreen.rmrtest.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvgreen.rmrtest.EXTRA_COLLECTION_ID
import com.mvgreen.rmrtest.EXTRA_PHOTO_URL
import com.mvgreen.rmrtest.R
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto
import com.mvgreen.rmrtest.view.PagingListAdapter
import com.mvgreen.rmrtest.viewmodel.CollectionContentViewModel
import kotlinx.android.synthetic.main.activity_collection_content.*
import kotlinx.android.synthetic.main.activity_collection_content.recycler
import java.lang.IllegalStateException

class CollectionContentActivity : AppCompatActivity() {

    private val viewModel: CollectionContentViewModel by lazy {
        ViewModelProviders.of(this).get(CollectionContentViewModel::class.java).apply {
            val id = intent.getIntExtra(EXTRA_COLLECTION_ID, -1)
            if (id == -1)
                throw IllegalStateException("EXTRA_COLLECTION_ID extra not found!")
            openCollection(id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_content)
        setSupportActionBar(toolbar)
        // RecyclerView setup
        with(recycler) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@CollectionContentActivity)
            adapter = PagingListAdapter(
                this@CollectionContentActivity,
                viewModel,
                viewModel.collectionContent,
                UnsplashPhoto::class.java
            ) { _, item ->
                startActivity(Intent(this@CollectionContentActivity, FullscreenActivity::class.java).apply {
                    putExtra(
                        EXTRA_PHOTO_URL,
                        item.urls.raw
                    )
                })
            }

            // Observe list to load new items on time
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    // Send notification to adapter
                    (adapter as PagingListAdapter<*>).updateListIfNeeded(lastItem)
                }
            })
        }

    }
}
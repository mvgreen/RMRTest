package com.mvgreen.rmrtest.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.mvgreen.rmrtest.EXTRA_COLLECTION_ID
import com.mvgreen.rmrtest.EXTRA_PHOTO_URL
import com.mvgreen.rmrtest.R
import com.mvgreen.rmrtest.fragment.ListFragment
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashCollection
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto
import com.mvgreen.rmrtest.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.toolbar_search.*


class SearchActivity : AppCompatActivity() {

    /**
     * The [androidx.viewpager.widget.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * androidx.fragment.app.FragmentStatePagerAdapter.
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private lateinit var viewModel: SearchViewModel
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        setContentView(R.layout.activity_search)

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_search, menu)
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty())
                    viewModel.performSearch(query)
                supportActionBar?.title = if (query.isNullOrEmpty()) supportActionBar?.title
                else query
                onBackPressed()
                onBackPressed()
                return true
            }

            override fun onQueryTextChange(newText: String?) = false
        })
        return true
    }

    override fun onBackPressed() {
        if (!searchView.isIconified)
            searchView.isIconified = true
        else
            super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val searchPhotoListFragment =
            ListFragment.newInstance(viewModel, viewModel.searchPhotoResult, UnsplashPhoto::class.java) { item, _ ->
                startActivity(Intent(this@SearchActivity, FullscreenActivity::class.java).apply {
                    putExtra(
                        EXTRA_PHOTO_URL,
                        item.urls.raw
                    )
                })
            }
        private val searchCollectionFragment =
            ListFragment.newInstance(viewModel, viewModel.searchCollectionResult, UnsplashCollection::class.java) { item, _ ->
                startActivity(Intent(this@SearchActivity, CollectionContentActivity::class.java).apply {
                    putExtra(EXTRA_COLLECTION_ID, item.id)
                })
            }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> searchPhotoListFragment
                1 -> searchCollectionFragment
                else -> throw IllegalArgumentException("Parameter 'position' must be either 0 or 1")
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }
}

package com.mvgreen.rmrtest.activity

import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.mvgreen.rmrtest.R
import com.mvgreen.rmrtest.fragment.ListFragment
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashCollection
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto
import com.mvgreen.rmrtest.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.toolbar_search.*

class SearchActivity : AppCompatActivity() {

    companion object {
        // Parameter name for savedInstanceState
        private const val SAVED_QUERY = "SAVED_QUERY"
        // Fragment IDs
        private const val FRAGMENT_PHOTOS = 0
        private const val FRAGMENT_COLLECTIONS = 1
    }

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    lateinit var searchView: SearchView
    private val viewModel: SearchViewModel by lazy { ViewModelProviders.of(this).get(SearchViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        viewModel.searchQuery = savedInstanceState?.getString(SAVED_QUERY, "") ?: ""
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
        // Request to re-initialize toolbar to restore the previous search
        invalidateOptionsMenu()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save search query to restore it later
        outState.putString(SAVED_QUERY, viewModel.searchQuery)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_search, menu)
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.isIconified = false
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(searchQuery: String?): Boolean {
                // Trim leading and trailing whitespaces
                val query = searchQuery?.trim()
                // Send search request
                if (!query.isNullOrEmpty())
                    viewModel.newQuery(query)
                else
                    return true
                // Display query text on toolbar
                supportActionBar?.title = if (query.isNullOrEmpty()) supportActionBar?.title else query
                // Collapse searchView after call, it needs to be called twice, at least in Android 5
                if (!searchView.isIconified) {
                    searchView.isIconified = true
                    searchView.isIconified = true
                }
                return true
            }

            override fun onQueryTextChange(newText: String?) = false
        })
        // Restore previous query, if present
        searchView.setQuery(viewModel.searchQuery, true)
        return true
    }

    override fun onBackPressed() {
        // Make searchView collapse if it's opened
        if (!searchView.isIconified)
            searchView.isIconified = true
        else
            super.onBackPressed()
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            // Initialize page fragments, the first shows photos, and the second shows collections
            return when (position) {
                FRAGMENT_PHOTOS -> ListFragment.newInstance<UnsplashPhoto>(
                    FRAGMENT_PHOTOS
                )
                FRAGMENT_COLLECTIONS -> ListFragment.newInstance<UnsplashCollection>(
                    FRAGMENT_COLLECTIONS
                )
                else -> throw IllegalArgumentException("Parameter 'position' must be either 0 or 1")
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }
}

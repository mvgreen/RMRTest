package com.mvgreen.rmrtest.activity

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
import com.mvgreen.rmrtest.R
import com.mvgreen.rmrtest.fragment.ListFragment
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashCollection
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto
import com.mvgreen.rmrtest.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.toolbar_search.*


class SearchActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private val viewModel: SearchViewModel by lazy { ViewModelProviders.of(this).get(SearchViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
    }

    override fun onResume() {
        super.onResume()
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
        viewModel.searchView = menu.findItem(R.id.action_search).actionView as SearchView
        viewModel.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty())
                    viewModel.newQuery(query)
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
        if (!viewModel.searchView.isIconified)
            viewModel.searchView.isIconified = true
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

        val FRAGMENT_PHOTOS = 0
        val FRAGMENT_COLLECTIONS = 1

        override fun getItem(position: Int): Fragment {
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

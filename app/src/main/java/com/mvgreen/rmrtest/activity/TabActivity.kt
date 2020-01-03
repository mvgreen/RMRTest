package com.mvgreen.rmrtest.activity

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.mvgreen.rmrtest.R
import com.mvgreen.rmrtest.fragment.SearchFragment
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashCollection
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto
import com.mvgreen.rmrtest.viewmodel.UnsplashViewModel
import kotlinx.android.synthetic.main.activity_tab.*
import kotlinx.android.synthetic.main.toolbar_search.*
import java.lang.IllegalArgumentException

class TabActivity : AppCompatActivity() {

    /**
     * The [androidx.viewpager.widget.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * androidx.fragment.app.FragmentStatePagerAdapter.
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private lateinit var viewModel: UnsplashViewModel
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(UnsplashViewModel::class.java)
        setContentView(R.layout.activity_tab)

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
        menuInflater.inflate(R.menu.menu_tab, menu)
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty())
                    viewModel.performSearch(query)
                supportActionBar?.title = when {
                    query.isNullOrEmpty() -> supportActionBar?.title
                    query.length > 15 -> query.substring(0, 16) + "..."
                    else -> query
                }
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

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> SearchFragment.newInstance(viewModel.searchPhotoResult, UnsplashPhoto::class.java)
                1 -> SearchFragment.newInstance(viewModel.searchCollectionResult, UnsplashCollection::class.java)
                else -> throw IllegalArgumentException("Parameter 'position' must be either 0 or 1")
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }
}

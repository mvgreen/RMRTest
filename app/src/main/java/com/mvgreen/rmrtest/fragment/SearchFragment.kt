package com.mvgreen.rmrtest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvgreen.rmrtest.R
import com.mvgreen.rmrtest.model.network.json_objects.ResultListItem
import com.mvgreen.rmrtest.view.PagingAdapter
import com.mvgreen.rmrtest.viewmodel.UnsplashViewModel
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment<T : ResultListItem> : Fragment() {

    companion object {
        fun <T : ResultListItem> newInstance(itemSource: LiveData<List<T>?>, itemType: Class<T>): SearchFragment<T> {
            return SearchFragment<T>().apply {
                this.itemSource = itemSource
                this.itemType = itemType
            }
        }
    }

    private lateinit var viewModel: UnsplashViewModel
    private lateinit var itemSource: LiveData<List<T>?>
    private lateinit var itemType: Class<T>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(UnsplashViewModel::class.java)

        // RecyclerView setup
        with(recycler) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@SearchFragment.context)
            adapter = PagingAdapter(this@SearchFragment, itemSource, itemType)

            // Observe list to load new items on time
            recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    // Send notification to adapter
                    (adapter as PagingAdapter<*>).updateListIfNeeded(lastItem)
                }
            })
        }
    }
}
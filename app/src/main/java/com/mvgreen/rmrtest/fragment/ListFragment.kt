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
import com.mvgreen.rmrtest.view.PagingListAdapter
import com.mvgreen.rmrtest.viewmodel.UnsplashViewModel
import kotlinx.android.synthetic.main.fragment_search.*

class ListFragment<T : ResultListItem> : Fragment() {

    companion object {
        fun <T : ResultListItem> newInstance(
            itemSource: LiveData<List<T>?>,
            itemType: Class<T>,
            onItemClick: (item: T, itemType: Class<T>) -> Unit
        ): ListFragment<T> {
            return ListFragment<T>().apply {
                this.itemSource = itemSource
                this.itemType = itemType
                this.onItemClick = onItemClick
            }
        }
    }

    private lateinit var viewModel: UnsplashViewModel
    private lateinit var itemSource: LiveData<List<T>?>
    private lateinit var itemType: Class<T>

    private lateinit var onItemClick: (item: T, itemType: Class<T>) -> Unit

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
            layoutManager = LinearLayoutManager(this@ListFragment.context)
            adapter = PagingListAdapter(this@ListFragment, itemSource, itemType, onItemClick)

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
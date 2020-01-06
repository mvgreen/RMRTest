package com.mvgreen.rmrtest.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvgreen.rmrtest.R
import com.mvgreen.rmrtest.model.network.json_objects.ResultListItem
import com.mvgreen.rmrtest.view.PagingListAdapter
import com.mvgreen.rmrtest.viewmodel.CollectionContentViewModel
import com.mvgreen.rmrtest.viewmodel.ListFragmentViewModel
import com.mvgreen.rmrtest.viewmodel.SearchViewModel
import com.mvgreen.rmrtest.viewmodel.UnsplashViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import java.lang.IllegalStateException

class ListFragment<T : ResultListItem> : Fragment() {

    companion object {
        private const val FRAGMENT_ID = "ARG_FRAGMENT_ID"

        fun <T : ResultListItem> newInstance(
            fragmentId: Int
        ): ListFragment<T> {
            return ListFragment<T>().apply {
                arguments = bundleOf(FRAGMENT_ID to fragmentId)
            }
        }
    }

    private val vm: ListFragmentViewModel by lazy { ViewModelProviders.of(this).get(ListFragmentViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm.fragmentId = arguments?.getInt(FRAGMENT_ID) ?: throw IllegalStateException("Fragment ID not found!")
        vm.activityViewModel = ViewModelProviders.of(activity!!).get(SearchViewModel::class.java)
        // RecyclerView setup
        with(recycler) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ListFragment.context)
            adapter = PagingListAdapter(
                this@ListFragment.activity!!,
                vm.activityViewModel,
                vm.getItemSource<T>(),
                vm.getItemType(),
                vm.getOnItemClick()
            )

            // Observe list to load new items on time
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastItem = (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    // Send notification to adapter
                    (adapter as PagingListAdapter<*>).updateListIfNeeded(lastItem)
                }
            })
        }
    }
}
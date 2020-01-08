package com.mvgreen.rmrtest.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.mvgreen.rmrtest.R
import com.mvgreen.rmrtest.model.network.json_objects.ResultListItem
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashCollection
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto
import com.mvgreen.rmrtest.viewmodel.LiveList
import com.mvgreen.rmrtest.viewmodel.UnsplashViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.live_list_item.view.*

/**
 * Adapter for [RecyclerView] that supports live updates of the list.
 * @property activity owner of the adapter's [RecyclerView].
 * @property viewModel ViewModel that contains [liveList].
 * @property liveList content of this adapter.
 * @property itemType class of items in [liveList].
 * @property onItemClick function that is called when the user selects an item.
 */
class PagingListAdapter<T : ResultListItem>(
    private val activity: FragmentActivity,
    private val viewModel: UnsplashViewModel,
    private val liveList: LiveList<T>,
    private val itemType: Class<T>,
    private val onItemClick: (context: Context, item: T) -> Unit
) :
    RecyclerView.Adapter<PagingListAdapter.ItemHolder>() {

    class ItemHolder(item: View) : RecyclerView.ViewHolder(item)

    // List to display
    private lateinit var items: List<UnsplashPhoto>
    // Actual list that can have different item type
    private lateinit var actualItems: List<T>
    // Support flag to prevent unwanted updates
    private var loadingInProgress = false

    init {
        // Set list items if already present
        updateItems(liveList.value)
        // Wait for updates
        liveList.observe(activity, Observer {
            // Accept next update request
            loadingInProgress = false
            // Check for errors
            val isBadResult = liveList.isBadResult

            // If the result is null, then request was a new search query, otherwise it was an update of the current list
            if (isBadResult) {
                if (it == null)
                    Toast.makeText(
                        activity,
                        "Server response is empty, check internet connection or try another query",
                        Toast.LENGTH_SHORT
                    ).show()
                else
                    Toast.makeText(
                        activity,
                        "Server response is empty, check internet connection and scroll the list another time",
                        Toast.LENGTH_SHORT
                    ).show()
            }
            // Update list content
            updateItems(it)
        })
    }

    /**
     * Update [RecyclerView] content with new elements.
     * @param newItems new content.
     */
    @Suppress("UNCHECKED_CAST")
    private fun updateItems(newItems: List<T>?) {
        // There can be two types of elements, cast the list according to itemType of this adapter
        when (itemType) {
            UnsplashPhoto::class.java -> {
                items = (newItems as List<UnsplashPhoto>?) ?: listOf()
                actualItems = (items as List<T>?) ?: listOf()
            }
            UnsplashCollection::class.java -> {
                actualItems = newItems ?: listOf()
                items = (actualItems as List<UnsplashCollection>).map { it.cover_photo }
            }
            else ->
                throw IllegalArgumentException("Unsupported ResultListItem implementation")
        }
        // Update view
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.live_list_item, parent, false)

        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        Picasso.get()
            .load(items[position].urls.small)
            .placeholder(activity.resources.getDrawable(R.drawable.baseline_image_24, null))
            .centerCrop()
            .fit()
            .into(holder.itemView.image_holder)

        holder.itemView.apply {
            title.text = if (itemType == UnsplashPhoto::class.java)
                items[position].description ?: "..."
            else
                (actualItems[position] as UnsplashCollection).title
            setOnClickListener { onItemClick(activity, actualItems[position]) }
        }
    }

    /**
     * Notify the adapter of the current last item so that it updates if necessary.
     * @param lastItem current last visible item in [RecyclerView].
     */
    fun updateListIfNeeded(lastItem: Int) {
        if (lastItem != items.size - 1 || loadingInProgress)
            return
        // Reject further requests until this is completed
        loadingInProgress = true
        Log.d("TEST", "START")
        // Request update
        viewModel.loadNextPageOf(liveList)
    }

}
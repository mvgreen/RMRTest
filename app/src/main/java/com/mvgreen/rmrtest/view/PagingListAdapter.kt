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

class PagingListAdapter<T : ResultListItem>(
    private val activity: FragmentActivity,
    private val viewModel: UnsplashViewModel,
    private val liveList: LiveList<T>,
    private val itemType: Class<T>,
    private val onItemClick: (context: Context, item: T) -> Unit
) :
    RecyclerView.Adapter<PagingListAdapter.ItemHolder>() {

    class ItemHolder(item: View) : RecyclerView.ViewHolder(item)

    private lateinit var items: List<UnsplashPhoto>
    private lateinit var actualItems: List<T>
    private var loadingInProgress = false

    init {
        updateItems(liveList.value)
        liveList.observe(activity, Observer {
            val isBadResult = liveList.isBadResult
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
            updateItems(it)
        })
    }

    @Suppress("UNCHECKED_CAST")
    private fun updateItems(newItems: List<T>?) {
        when (itemType) {
            UnsplashPhoto::class.java -> {
                items = (newItems as List<UnsplashPhoto>?) ?: listOf()
                actualItems = (items as List<T>?) ?: listOf()
                Log.d("PAGING ADAPTER", "source is photo list")
            }
            UnsplashCollection::class.java -> {
                actualItems = newItems ?: listOf()
                items = (actualItems as List<UnsplashCollection>).map { it.cover_photo }
                Log.d("PAGING ADAPTER", "source is collection list")
            }
            else ->
                throw IllegalArgumentException("Unsupported ResultListItem implementation")
        }
        notifyDataSetChanged()
        loadingInProgress = false
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

    fun updateListIfNeeded(lastItem: Int) {
        if (lastItem != items.size - 1 || loadingInProgress)
            return
        //Toast.makeText(fragment.context, "Loading next page...", Toast.LENGTH_SHORT).show()
        loadingInProgress = true
        viewModel.loadNextPageOf(liveList)
    }

}
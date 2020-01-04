package com.mvgreen.rmrtest.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.mvgreen.rmrtest.R
import com.mvgreen.rmrtest.model.network.json_objects.ResultListItem
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashCollection
import com.mvgreen.rmrtest.model.network.json_objects.UnsplashPhoto
import com.mvgreen.rmrtest.viewmodel.UnsplashViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.live_list_item.view.*

class PagingAdapter<T : ResultListItem>(
    private val fragment: Fragment,
    private val recyclerView: RecyclerView,
    private val liveData: LiveData<List<T>?>,
    private val itemType: Class<T>
) :
    RecyclerView.Adapter<PagingAdapter.ItemHolder>() {

    class ItemHolder(item: View) : RecyclerView.ViewHolder(item)

    private lateinit var items: List<UnsplashPhoto>
    private var loadingInProgress = false

    init {
        updateItems(liveData.value)
        liveData.observe(fragment, Observer {
            if (it.isNullOrEmpty())
                Toast.makeText(
                    fragment.context,
                    "Server response is empty, check internet connection and make another query",
                    Toast.LENGTH_LONG
                ).show()
            if (it == items)
                Toast.makeText(
                    fragment.context,
                    "Server response is empty, check internet connection and scroll the list another time",
                    Toast.LENGTH_LONG
                ).show()
            updateItems(it)
        })

    }

    @Suppress("UNCHECKED_CAST")
    private fun updateItems(newItems: List<T>?) {
        when (itemType) {
            UnsplashPhoto::class.java -> {
                items = (newItems as List<UnsplashPhoto>?) ?: listOf()
                Log.d("PAGING ADAPTER", "source is photo list")
            }
            UnsplashCollection::class.java -> {
                items = (newItems as List<UnsplashCollection>?)?.map { it.cover_photo } ?: listOf()
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
            .placeholder(fragment.resources.getDrawable(R.drawable.baseline_image_24, null))
            .centerCrop()
            .fit()
            .into(holder.itemView.image_holder)
    }

    fun updateListIfNeeded(lastItem: Int) {
        if (lastItem != items.size - 1 || loadingInProgress)
            return
        //Toast.makeText(fragment.context, "Loading next page...", Toast.LENGTH_SHORT).show()
        loadingInProgress = true
        val vm = ViewModelProviders.of(fragment.activity!!).get(UnsplashViewModel::class.java)
        vm.loadNextPageOf(liveData)
    }

}
package com.mvgreen.rmrtest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvgreen.rmrtest.R
import com.mvgreen.rmrtest.viewmodel.UnsplashViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.text_item.view.*

class SearchFragment: Fragment() {

    private lateinit var viewModel: UnsplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(UnsplashViewModel::class.java)

        with(recycler) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@SearchFragment.context)
            adapter = ListAdapter(listOf("item 1", "item 2", "item 3", "item 4", "item 5", "item 6", "item 7", "item 8"))
        }
    }

    class ListAdapter(private val dataset: List<String>) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

        class ListViewHolder(textView: TextView) : RecyclerView.ViewHolder(textView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
            val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.text_item, parent, false) as TextView

            return ListViewHolder(textView)
        }

        override fun getItemCount(): Int {
            return dataset.size
        }

        override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
            holder.itemView.textView.text = dataset[position]
        }

    }
}
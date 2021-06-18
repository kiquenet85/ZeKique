package com.example.zemogatest.presentation.post.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.zemogatest.databinding.PostListItemBinding
import com.example.zemogatest.presentation.base.CounterDiffUtil
import com.example.zemogatest.presentation.post.list.ui_model.PostUI

class PostAdapter(
    private val items: MutableList<PostUI> = mutableListOf(),
    private val listener: OnPostListener
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(val binding: PostListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            PostListItemBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = items[position]

        with(viewHolder.binding) {
            containerListItem.setOnClickListener {
                seenImage.visibility = View.GONE
                listener.onPostClicked(item)
            }

            seenImage.visibility = if (item.showAsNotSeen) View.VISIBLE else View.GONE
            description.text = item.description
            favoriteImage.visibility = if (item.favorite) View.VISIBLE else View.GONE
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addNewItems(allItems: List<PostUI>) {
        val diffResult = DiffUtil.calculateDiff(CounterDiffUtil(items, allItems))

        this.items.clear()
        this.items.addAll(allItems)
        diffResult.dispatchUpdatesTo(this)
    }

    fun clearAllItems() {
        items.clear()
        notifyDataSetChanged()
    }

    interface OnPostListener {
        fun onPostClicked(post: PostUI)
    }
}
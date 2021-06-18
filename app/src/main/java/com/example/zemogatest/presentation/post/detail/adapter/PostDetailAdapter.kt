package com.example.zemogatest.presentation.post.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.zemogatest.databinding.PostDetailListItemBinding
import com.example.zemogatest.presentation.base.CounterDiffUtil
import com.example.zemogatest.presentation.base.Identifier
import com.example.zemogatest.presentation.post.detail.ui_model.CommentUI

class PostDetailAdapter(
    private val items: MutableList<CommentUI> = mutableListOf()
) : RecyclerView.Adapter<PostDetailAdapter.ViewHolder>() {

    class ViewHolder(val binding: PostDetailListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            PostDetailListItemBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = items[position]
        viewHolder.binding.postDetailCommentDescription.text = item.body
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addNewItems(allItems: List<CommentUI>) {
        val diffResult = DiffUtil.calculateDiff(CounterDiffUtil(items, allItems))

        this.items.clear()
        this.items.addAll(allItems)
        diffResult.dispatchUpdatesTo(this)
    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }
}
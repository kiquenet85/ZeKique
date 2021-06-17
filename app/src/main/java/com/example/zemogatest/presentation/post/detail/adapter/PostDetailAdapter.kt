package com.example.zemogatest.presentation.post.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.zemogatest.databinding.PostDetailListItemBinding
import com.example.zemogatest.presentation.base.CounterDiffUtil
import com.example.zemogatest.presentation.base.Identifier

class PostDetailAdapter(
    private val items: MutableList<PostCommentUI> = mutableListOf()
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
        viewHolder.binding.postDetailCommentDescription.text = item.value
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addNewItems(allItems: List<PostCommentUI>) {
        val diffResult = DiffUtil.calculateDiff(CounterDiffUtil(items, allItems))

        this.items.clear()
        this.items.addAll(allItems)
        diffResult.dispatchUpdatesTo(this)
    }
}

class PostCommentUI(val value: String) : Identifier {
    override fun getIdentifier(): String {
        return value
    }
}
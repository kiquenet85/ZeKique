package com.example.zemogatest.presentation.post.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.zemogatest.presentation.base.CounterDiffUtil
import com.example.zemogatest.presentation.base.Identifier
import com.example.zemogatest.databinding.PostListItemBinding

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
        viewHolder.binding.containerListItem.setOnClickListener {
            listener.onPostClicked(item)
        }
        viewHolder.binding.description.text = item.value
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

    interface OnPostListener {
        fun onPostClicked(post: PostUI)
    }
}

class PostUI(val value: String): Identifier {
    override fun getId(): String {
        return value
    }
}
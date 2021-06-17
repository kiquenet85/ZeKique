package com.example.zemogatest.presentation.post.list

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.zemogatest.R

/**
 *  Page pagerAdapter containing posts
 * @author n.diazgranados
 */
class PostPagerAdapter(fragManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragManager, lifecycle) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return FragmentPostList.newInstance(
            if (FAVORITE_POSTS == position) {
                PostViewModel.Filter.FAVORITES
            } else {
                PostViewModel.Filter.NONE
            }
        )
    }

    fun getTitles() = arrayOf(
        R.string.posts_tab_title,
        R.string.favorites_tab_title
    )

    companion object {
        const val ALL_POSTS = 0
        const val FAVORITE_POSTS = 1
    }
}
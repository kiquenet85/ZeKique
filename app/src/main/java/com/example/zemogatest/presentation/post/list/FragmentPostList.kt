package com.example.zemogatest.presentation.post.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zemogatest.R
import com.example.zemogatest.databinding.FragmentPostListBinding
import com.example.zemogatest.presentation.post.detail.FragmentPostDetail
import com.example.zemogatest.presentation.post.list.adapter.PostAdapter
import com.example.zemogatest.presentation.post.list.adapter.PostUI
import com.example.zemogatest.presentation.welcome.MainActivity
import com.example.zemogatest.util.MarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

/**
 * A placeholder fragment containing a simple view.
 */
@AndroidEntryPoint
class FragmentPostList : Fragment(), PostAdapter.OnPostListener {

    private var binding: FragmentPostListBinding? = null
    private lateinit var postAdapter: PostAdapter

    enum class Filter {
        NONE, FAVORITES
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostListBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPosts()
    }

    private fun setUpPosts() {
        val mutableList = mutableListOf<PostUI>().apply {
            for (i in 0 until 100) {
                add(PostUI(Random.nextLong().toString()))
            }
        }
        postAdapter = PostAdapter(mutableList, this)
        binding?.postList?.apply {
            layoutManager = LinearLayoutManager(context)

            addItemDecoration(
                MarginItemDecoration(
                    marginTop = resources.getDimension(R.dimen.margin_normal_1).toInt(),
                    marginSides = resources.getDimension(R.dimen.margin_normal_1).toInt()
                )
            )
            adapter = postAdapter
        }
    }

    override fun onPostClicked(post: PostUI) {
        (activity as? MainActivity)?.pushFragment(FragmentPostDetail.newInstance(), true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.refresh_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                return true
            }
        }
        return false
    }

    companion object {
        private const val FILTER_TYPE = "FILTER_TYPE"

        @JvmStatic
        fun newInstance(filter: Filter): FragmentPostList {
            return FragmentPostList().apply {
                arguments = Bundle().apply {
                    putInt(FILTER_TYPE, filter.ordinal)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
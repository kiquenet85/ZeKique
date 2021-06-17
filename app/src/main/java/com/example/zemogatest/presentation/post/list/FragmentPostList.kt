package com.example.zemogatest.presentation.post.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zemogatest.R
import com.example.zemogatest.databinding.FragmentPostListBinding
import com.example.zemogatest.presentation.post.detail.FragmentPostDetail
import com.example.zemogatest.presentation.post.list.adapter.PostAdapter
import com.example.zemogatest.presentation.post.list.state.PostUI
import com.example.zemogatest.presentation.welcome.MainActivity
import com.example.zemogatest.util.MarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint

/**
 * A placeholder fragment containing a simple view.
 */
@AndroidEntryPoint
class FragmentPostList : Fragment(), PostAdapter.OnPostListener {

    private var binding: FragmentPostListBinding? = null
    private lateinit var postAdapter: PostAdapter
    private val viewModel: PostViewModel by viewModels()

    enum class Filter {
        NONE, FAVORITES
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.loadPosts(true)
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
        setUpListeners()
    }

    private fun setUpListeners() {
        viewModel.getScreenState().observe(viewLifecycleOwner) {
            when (it) {
                PostUIState.PostEmpty -> {

                }
                is PostUIState.PostLoaded -> {
                    postAdapter.addNewItems(it.value)
                }
                PostUIState.PostLoading -> {

                }
            }
        }
    }

    private fun setUpPosts() {
        postAdapter = PostAdapter(mutableListOf(), this)
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
        (activity as? MainActivity)?.navigator?.navigateTo(FragmentPostDetail.newInstance(), true)
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
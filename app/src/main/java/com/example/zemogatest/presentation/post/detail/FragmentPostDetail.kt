package com.example.zemogatest.presentation.post.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zemogatest.R
import com.example.zemogatest.databinding.FragmentPostDetailBinding
import com.example.zemogatest.presentation.post.detail.adapter.PostDetailAdapter
import com.example.zemogatest.presentation.post.list.ui_model.PostUI
import com.example.zemogatest.util.EMPTY_STRING
import com.example.zemogatest.util.MarginItemDecoration
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentPostDetail : Fragment() {

    private var binding: FragmentPostDetailBinding? = null
    private lateinit var postDetailAdapter: PostDetailAdapter
    private val viewModel: PostDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.setSelectedPost(requireArguments().getParcelable(EXTRA_SELECTED_POST))
        viewModel.loadComments(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.GONE
        setUpPostInformation()
        setUpComments()
        setUpListeners()
    }

    private fun setUpPostInformation() {
        binding?.fragmentPostDetailDescriptionTitle?.text = viewModel.post?.title ?: EMPTY_STRING
        binding?.fragmentPostDetailDescriptionMsg?.text =
            viewModel.post?.description ?: EMPTY_STRING
        binding?.fragmentPostDetailUserTitle?.text = getString(R.string.post_detail_user_title)
        binding?.fragmentPostDetailUserName?.text = viewModel.post?.userUI?.name ?: EMPTY_STRING
        binding?.fragmentPostDetailUserEmail?.text = viewModel.post?.userUI?.email ?: EMPTY_STRING
        binding?.fragmentPostDetailUserPhone?.text = viewModel.post?.userUI?.phone ?: EMPTY_STRING
        binding?.fragmentPostDetailUserWebsite?.text =
            viewModel.post?.userUI?.website ?: EMPTY_STRING
        binding?.fragmentPostDetailCommentsTitle?.text = getString(R.string.post_detail_comments_title)
    }

    private fun setUpComments() {
        postDetailAdapter = PostDetailAdapter(mutableListOf())
        binding?.fragmentPostDetailCommentsList?.apply {
            layoutManager = LinearLayoutManager(context)

            addItemDecoration(
                MarginItemDecoration(
                    marginTop = resources.getDimension(R.dimen.margin_normal_1).toInt(),
                    marginSides = resources.getDimension(R.dimen.margin_normal_1).toInt()
                )
            )
            adapter = postDetailAdapter
        }
    }

    private fun setUpListeners() {
        viewModel.getScreenState().observe(viewLifecycleOwner) {
            when (it) {
                CommentUIState.CommentEmpty ->
                    postDetailAdapter.clearItems()
                is CommentUIState.CommentLoaded ->
                    postDetailAdapter.addNewItems(it.value)
                CommentUIState.CommentLoading -> {
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.favorite_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {
                item.icon =
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_favorite_24, null)
                return true
            }
        }
        return false
    }

    companion object {
        private const val EXTRA_SELECTED_POST = "EXTRA_SELECTED_POST"

        @JvmStatic
        fun newInstance(postUI: PostUI) = FragmentPostDetail().apply {
            arguments = Bundle().apply {
                putParcelable(EXTRA_SELECTED_POST, postUI)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
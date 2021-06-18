package com.example.zemogatest.presentation.post.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zemogatest.R
import com.example.zemogatest.databinding.FragmentPostDetailBinding
import com.example.zemogatest.presentation.base.CommonErrorState
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
        requireActivity().invalidateOptionsMenu()
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
        binding?.fragmentPostDetailCommentsTitle?.text =
            getString(R.string.post_detail_comments_title)
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
        viewModel.getErrorState().observe(viewLifecycleOwner) {
            binding?.progressBar?.visibility = View.GONE
            when (it) {
                is CommonErrorState.ConnectionError ->
                    Toast.makeText(
                        context,
                        getString(R.string.connection_error_description),
                        Toast.LENGTH_SHORT
                    ).show()
                is CommonErrorState.CriticalUIError,
                is CommonErrorState.LogicalError ->
                    Toast.makeText(
                        context,
                        getString(R.string.general_error_handled),
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }

        viewModel.getScreenState().observe(viewLifecycleOwner) {
            when (it) {
                CommentUIState.CommentEmpty -> {
                    binding?.progressBar?.visibility = View.GONE
                    postDetailAdapter.clearItems()
                }
                is CommentUIState.CommentLoaded -> {
                    binding?.progressBar?.visibility = View.GONE
                    postDetailAdapter.addNewItems(it.value)
                }
                CommentUIState.CommentLoading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.favorite_menu, menu)
        if (viewModel.post?.favorite == true) {
            menu.findItem(R.id.action_favorite)?.icon =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_favorite_24, null)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {
                item.icon = if (viewModel.post?.favorite == true) {
                    viewModel.addPostAsFavorite(false)
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_baseline_favorite_unselect_24,
                        null
                    )
                } else {
                    viewModel.addPostAsFavorite(true)
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_baseline_favorite_24,
                        null
                    )
                }
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
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zemogatest.R
import com.example.zemogatest.databinding.FragmentPostDetailBinding
import com.example.zemogatest.presentation.post.detail.adapter.PostCommentUI
import com.example.zemogatest.presentation.post.detail.adapter.PostDetailAdapter
import com.example.zemogatest.util.MarginItemDecoration
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class FragmentPostDetail : Fragment() {

    private var binding: FragmentPostDetailBinding? = null
    private lateinit var postAdapter: PostDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        setUpComments()
    }

    private fun setUpComments() {
        val mutableList = mutableListOf<PostCommentUI>().apply {
            for (i in 0 until 100) {
                add(PostCommentUI(Random.nextLong().toString()))
            }
        }
        postAdapter = PostDetailAdapter(mutableList)
        binding?.fragmentPostDetailCommentsList?.apply {
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
        @JvmStatic
        fun newInstance() = FragmentPostDetail()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
package com.example.zemogatest.presentation.post.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.zemogatest.databinding.FragmentPostTabsBinding
import com.example.zemogatest.presentation.post.list.adapter.PostPagerAdapter
import com.example.zemogatest.presentation.post.list.adapter.PostPagerAdapter.Companion.ALL_POSTS
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentPostTabs : Fragment() {

    private lateinit var binding: FragmentPostTabsBinding
    private lateinit var pagerAdapter: PostPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentPostTabsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewPager()
    }

    private fun setUpViewPager() {
        pagerAdapter = PostPagerAdapter(childFragmentManager, lifecycle)
        with(binding.viewPager) {
            adapter = pagerAdapter
            TabLayoutMediator(binding.tabs, this) { tab, position ->
                val titles = pagerAdapter.getTitles()
                tab.text = getString(titles[position])
            }.attach()

            setCurrentItem(ALL_POSTS, false)
            offscreenPageLimit = 2
        }
    }

    companion object {
        fun newInstance() = FragmentPostTabs()
    }
}
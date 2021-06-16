package com.example.zemogatest.presentation.posts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.zemogatest.R
import com.example.zemogatest.databinding.ActivityMainBinding
import com.example.zemogatest.presentation.post.list.PostPagerAdapter
import com.example.zemogatest.presentation.post.list.PostPagerAdapter.Companion.ALL_POSTS
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pagerAdapter: PostPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()
        setUpViewPager()
        setUpFabButton()
    }

    private fun setUpToolbar() {
        with(binding.toolbar.toolbar) {
            setSupportActionBar(this)
            setNavigationOnClickListener {
                onBackPressed()
            }
            supportActionBar?.title = getString(R.string.posts_title)
        }
    }

    private fun setUpFabButton() {
        val fab: FloatingActionButton = binding.fab

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun setUpViewPager() {
        pagerAdapter = PostPagerAdapter(supportFragmentManager, lifecycle)
        with(binding.viewPager) {
            adapter = pagerAdapter
            TabLayoutMediator(binding.tabs, this) { tab, position ->
                val titles = pagerAdapter.getTitles()
                tab.text = getString(titles[position])
            }.attach()

            setCurrentItem(ALL_POSTS, false)
        }
    }
}
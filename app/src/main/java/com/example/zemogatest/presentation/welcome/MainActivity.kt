package com.example.zemogatest.presentation.welcome

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.zemogatest.R
import com.example.zemogatest.common.navigator.Navigator
import com.example.zemogatest.databinding.ActivityAppbarFabContainerBinding
import com.example.zemogatest.presentation.post.list.FragmentPostTabs
import com.example.zemogatest.presentation.post.list.PostViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppbarFabContainerBinding

    private val viewModel: PostViewModel by viewModels()
    @Inject lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppbarFabContainerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setUpToolbar()
        setUpFabButton()
        navigator.navigateTo(FragmentPostTabs.newInstance(), false)
    }

    private fun setUpFabButton() {
        val fab: FloatingActionButton = binding.fab

        fab.setOnClickListener { view ->
            viewModel.deleteAllPosts()
        }
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
}

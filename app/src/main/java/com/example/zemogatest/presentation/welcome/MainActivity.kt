package com.example.zemogatest.presentation.welcome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.zemogatest.R
import com.example.zemogatest.common.navigator.Navigator
import com.example.zemogatest.databinding.ActivityAppbarFabContainerBinding
import com.example.zemogatest.presentation.post.list.FragmentPostTabs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppbarFabContainerBinding

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
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
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
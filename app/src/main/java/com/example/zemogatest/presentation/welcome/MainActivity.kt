package com.example.zemogatest.presentation.welcome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.zemogatest.R
import com.example.zemogatest.databinding.ActivityAppbarFabContainerBinding
import com.example.zemogatest.presentation.post.list.FragmentPostTabs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppbarFabContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppbarFabContainerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setUpToolbar()
        setUpFabButton()
        pushFragment(FragmentPostTabs.newInstance(), false)
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

    fun pushFragment(
        fragment: Fragment,
        addToBackStack: Boolean,
        fragmentManager: FragmentManager = supportFragmentManager,
        containerId: Int = R.id.frag_container,
        tag: String = fragment::class.java.canonicalName ?: fragment::class.java.name,
        usingAddTransaction: Boolean = false
    ): Int {

        val transaction = fragmentManager.beginTransaction()
        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }

        return if (usingAddTransaction) {
            transaction.add(containerId, fragment, tag).commitAllowingStateLoss()
        } else {
            transaction.replace(containerId, fragment, tag).commitAllowingStateLoss()
        }
    }
}
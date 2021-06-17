package com.example.zemogatest.common.navigator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.zemogatest.R
import com.example.zemogatest.common.error.ErrorHandler
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class Navigator @Inject constructor(
    @ActivityContext context: Context
) {

    lateinit var errorHandler: ErrorHandler
    private val activity: AppCompatActivity = context as AppCompatActivity

    fun navigateTo(fragment: Fragment, addToBackStack: Boolean = true) {
        try {
            Log.v(
                Navigator::class.java.simpleName,
                "Navigating to Fragment ${fragment::class.java.canonicalName}"
            )
            pushFragment(
                fragment,
                addToBackStack,
            )
        } catch (exception: Exception) {
            errorHandler.reportCriticalException(
                exception,
                "Navigator produced an exception when inserting fragment!"
            )
        }
    }

    fun navigateTo(context: Context, javaClass: Class<*>, extras: Bundle? = null) {
        try {
            Log.v(
                Navigator::class.java.simpleName,
                "Navigating to AppCompatActivity ${javaClass.canonicalName}"
            )
            val intent = Intent(context, javaClass)
            extras?.let {
                intent.putExtras(it)
            }
            activity.startActivity(intent)
        } catch (exception: Exception) {
            errorHandler.reportCriticalException(
                exception,
                "Navigator produced an exception when inserting an activity!"
            )
        }
    }

    fun cleanBackStackOperation() {
        activity.supportFragmentManager.let {
            if (!it.isStateSaved && it.backStackEntryCount > 0) {
                val firstFragment = it.getBackStackEntryAt(0)
                it.popBackStack(firstFragment.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            } else {
                if (!it.isStateSaved) {
                    errorHandler.report(IllegalStateException("There is no backStack to clean the history on ${Navigator::class.java.simpleName}"))
                } else {
                    errorHandler.report(IllegalStateException("The fragment manager already saved the state ${it.isStateSaved}"))
                }
            }
        }
    }

    private fun pushFragment(
        fragment: Fragment,
        addToBackStack: Boolean,
        fragmentManager: FragmentManager = activity.supportFragmentManager,
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